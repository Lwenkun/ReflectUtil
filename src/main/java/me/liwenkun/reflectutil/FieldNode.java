package me.liwenkun.reflectutil;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class FieldNode extends ExprNode {

    @NotNull
    private final Field field;
    private final int paramCount;

    FieldNode(@NotNull Field field, int paramCount) {
        this.field = field;
        this.paramCount = paramCount;
    }

    @Override
    public Object evaluate(Object instance, Object... params) throws EvaluateException {
        try {
            if (paramCount == 1) {
                Object replace = (params != null && params.length >= 1) ? params[0] : null;
                if (replace == null) {
                    throw new IllegalArgumentException("field " + field
                            + " requires a value to replace");
                }
                field.set(instance, replace);
                return replace;
            } else {
                return field.get(instance);
            }
        } catch (IllegalAccessException e) {
            throw new EvaluateException("failed to evaluate field " + field, e);
        }
    }

    @Override
    public int getParamCount() {
        return paramCount;
    }

    @Override
    public String toString() {
        return "field node: " + field;
    }
}
