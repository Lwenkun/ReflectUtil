package me.liwenkun.reflectutil;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {

    private static final Pattern METHOD_PATTERN = Pattern.compile("(.+)\\((\\d*)\\)");

    private final ExprNode head;

    private Expression(ExprNode nodes) {
        this.head = nodes;
    }

    public Object evaluate(Object instance, Object... params) throws EvaluateException {
        ExprNode cur = head;
        Object tmpInstance = instance;
        Object result = null;
        int paramIndex = 0;
        while (cur != null) {
            int paramCount = cur.getParamCount();
            Object[] consumedParams = new Object[paramCount];
            System.arraycopy(params, paramIndex, consumedParams, 0, paramCount);
            for (int i = 0; i < paramCount; i++) {
                if (consumedParams[i] instanceof StaticTypeValue) {
                    consumedParams[i] = ((StaticTypeValue) consumedParams[i]).value();
                }
            }
            paramIndex += paramCount;
            result = cur.evaluate(tmpInstance, consumedParams);
            tmpInstance = result;
            cur = cur.next();
        }
        return result;
    }

    static Expression create(Class<?> cls, String expression, boolean recursive, Object... params)
            throws ReflectionException {
        if (StringUtil.isEmpty(expression)) {
            throw new ReflectionException("empty expression");
        }
        String[] nodes = expression.split("\\.");
        if (nodes.length == 0) {
            throw new ReflectionException("not a valid expression");
        }

        int paramIndex = 0;

        Class<?> nextOwnerType = cls;
        ExprNode head = null;
        ExprNode cur = null;
        ExprNode newNode;
        for (String node : nodes) {
            Matcher matcher = METHOD_PATTERN.matcher(node);
            if (matcher.find()) {
                int paramCount = StringUtil.isEmpty(matcher.group(2))
                        ? 0 : Integer.parseInt(matcher.group(2));
                String methodName = matcher.group(1);
                if (StringUtil.isEmpty(methodName)) {
                    throw new IllegalArgumentException("empty method name");
                }
                Class<?>[] paramType = new Class[paramCount];
                for (int i = paramIndex, j = 0; i < paramIndex + paramCount; i++, j++) {
                    Object value = params[i];
                    if (value instanceof StaticTypeValue) {
                        paramType[j] = ((StaticTypeValue) value).type();
                    } else  {
                        paramType[j] = value.getClass();
                    }
                }
                Method method;
                try {
                    method = recursive ? findMethodRecursive(nextOwnerType, methodName, paramType)
                            : nextOwnerType.getDeclaredMethod(methodName, paramType);
                } catch (NoSuchMethodException e) {
                    throw new ReflectionException("failed to reflect method " + methodName, e);
                }
                method.setAccessible(true);
                newNode = new MethodNode(method);
                paramIndex += paramCount;
                nextOwnerType = method.getReturnType();
            } else {
                String fieldName;
                boolean hasReplaceValue;
                fieldName = (hasReplaceValue = node.startsWith(":")) ? node.substring(1) : node;
                Field field;
                try {
                    field = recursive ? findFieldRecursive(nextOwnerType, fieldName)
                            : nextOwnerType.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    throw new ReflectionException("failed to reflect field " + fieldName, e);
                }
                field.setAccessible(true);
                newNode = new FieldNode(field, hasReplaceValue ? 1 : 0);
                if (hasReplaceValue) {
                    Class<?> paramsType = params[paramIndex].getClass();
                    if (!field.getType().isAssignableFrom(paramsType)
                            && !ClassUtil.isPrimitiveAndWrapper(field.getType(), paramsType)) {
                        throw new IllegalArgumentException("param " + params[paramIndex]
                                + " cannot assign to field[" + field + "]");
                    }
                    paramIndex++;
                    nextOwnerType = paramsType;
                } else {
                    nextOwnerType = field.getType();
                }
            }

            if (cur == null) {
                head = newNode;
            } else {
                cur.setNext(newNode);
            }
            cur = newNode;
        }
        return new Expression(head);
    }

    @NotNull
    private static Method findMethodRecursive(Class<?> cls, String methodName,
                                              Class<?>... paramTypes) throws NoSuchMethodException {
        Method method = null;
        Class<?> searchCls = cls;
        Class<?> finalClx = cls;
        while (method == null && searchCls != null) {
            searchCls = mapToWrapperIfNecessary(searchCls);
            try {
                method = searchCls.getDeclaredMethod(methodName, paramTypes);
            } catch (NoSuchMethodException ignore) {
                finalClx = searchCls;
                searchCls = searchCls.getSuperclass();
            }
        }
        if (method == null) {
            throw new NoSuchMethodException("cannot find method " + methodName + " from " + cls
                    + " to " + finalClx);
        }
        return method;
    }

    @NotNull
    private static Field findFieldRecursive(Class<?> cls, String fieldName)
            throws NoSuchFieldException {
        Field field = null;
        Class<?> searchCls = cls;
        Class<?> finalCls = cls;
        while (field == null && searchCls != null) {
            searchCls = mapToWrapperIfNecessary(searchCls);
            try {
                field = searchCls.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignore) {
                finalCls = searchCls;
                searchCls = searchCls.getSuperclass();
            }
        }
        if (field == null) {
            throw new NoSuchFieldException("cannot find field " + fieldName + " from " + cls
                    + " to " + finalCls);
        }
        return field;
    }

    @NotNull
    private static Class<?> mapToWrapperIfNecessary(@NotNull Class<?> type) {
        return ClassUtil.isPrimitive(type) ? ClassUtil.wrapperOf(type) : type;
    }
}
