package me.liwenkun.reflectutil;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class ClassUtil {
    private static final Map<Class<?>, Class<?>> sPrimitiveWrapperClassMap = new HashMap<>();
    private static final Map<Class<?>, Class<?>> sWrapperPrimitiveClassMap = new HashMap<>();

    static {
        sPrimitiveWrapperClassMap.put(byte.class, Byte.class);
        sPrimitiveWrapperClassMap.put(short.class, Short.class);
        sPrimitiveWrapperClassMap.put(int.class, Integer.class);
        sPrimitiveWrapperClassMap.put(long.class, Long.class);
        sPrimitiveWrapperClassMap.put(float.class, Float.class);
        sPrimitiveWrapperClassMap.put(double.class, Double.class);
        sPrimitiveWrapperClassMap.put(boolean.class, Boolean.class);

        for (Map.Entry<Class<?>, Class<?>> e : sPrimitiveWrapperClassMap.entrySet()) {
            sWrapperPrimitiveClassMap.put(e.getValue(), e.getKey());
        }
    }

    @NotNull
    static Class<?> primitiveOf(Class<?> wrapperClass) {
        Class<?> primitiveClass = sWrapperPrimitiveClassMap.get(wrapperClass);
        if (primitiveClass == null) {
            throw new IllegalArgumentException(wrapperClass + " is not a wrapper class");
        }
        return primitiveClass;
    }

    @NotNull
    static Class<?> wrapperOf(Class<?> primitiveClass) {
        Class<?> wrapperClass = sPrimitiveWrapperClassMap.get(primitiveClass);
        if (wrapperClass == null) {
            throw new IllegalArgumentException(primitiveClass + " is not a primitive class");
        }
        return wrapperClass;
    }

    static boolean isPrimitive(Class<?> cls) {
        return sPrimitiveWrapperClassMap.containsKey(cls);
    }

    static boolean isWrapper(Class<?> cls) {
        return sWrapperPrimitiveClassMap.containsKey(cls);
    }

    static boolean isPrimitiveAndWrapper(Class<?> first, Class<?> second) {
        if (isPrimitive(first)) {
            return wrapperOf(first) == second;
        } else if (isWrapper(first)) {
            return primitiveOf(first) == second;
        } else {
            return false;
        }
    }

    @NotNull
    static Method findMethodRecursive(Class<?> cls, String methodName,
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
    static Field findFieldRecursive(Class<?> cls, String fieldName)
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
