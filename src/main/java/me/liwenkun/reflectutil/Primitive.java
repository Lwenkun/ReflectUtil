package me.liwenkun.reflectutil;

public class Primitive {

    private final Object value;
    private final Class<?> type;

    public static Primitive of(Object value) {
        return new Primitive(value);
    }

    private Primitive(Object value) {
        this.value = value;
        type = parseType();
    }

    private Class<?> parseType() {
        if (value instanceof Byte) {
            return byte.class;
        } else if (value instanceof Short) {
            return short.class;
        } else if (value instanceof Integer) {
            return int.class;
        } else if (value instanceof Long) {
            return long.class;
        } else if (value instanceof Float) {
            return float.class;
        } else if (value instanceof Double) {
            return double.class;
        } else if (value instanceof Boolean) {
            return boolean.class;
        } else {
            throw new IllegalArgumentException("not a primitive value");
        }
    }

    public Class<?> type() {
        return type;
    }

    public Object value() {
        return value;
    }
}