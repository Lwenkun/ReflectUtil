package me.liwenkun.reflectutil;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodNode extends ExprNode {

    @NotNull
    private final Method method;

    MethodNode(@NotNull Method method) {
        this.method = method;
    }

    @Override
    public Object evaluate(Object instance, Object... params) throws EvaluateException {
        try {
            return method.invoke(instance, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
           throw new EvaluateException("failed to execute method " + method, e);
        }
    }

    @Override
    public int getParamCount() {
        return method.getParameterTypes().length;
    }

    @Override
    public String toString() {
        return "method node: " + method;
    }
}
