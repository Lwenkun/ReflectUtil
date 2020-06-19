package me.liwenkun.reflectutil;

public class Cast {
    private final Class<?> cls;
    private final Object value;

    static Cast of(Object value, Class<?> castTo) {
        return new Cast(value, castTo);
    }

    private Cast(Object value, Class<?> castTo) {
        this.value = value;
        this.cls = castTo;
    }

    public Class<?> type() {
        return cls;
    }

    public Object value() {
        return value;
    }
}