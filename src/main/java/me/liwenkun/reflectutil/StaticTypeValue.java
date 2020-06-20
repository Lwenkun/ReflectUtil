package me.liwenkun.reflectutil;

class StaticTypeValue {
    private final Class<?> staticType;
    private final Object value;

    StaticTypeValue(Object value, Class<?> staticType) {
        this.value = value;
        this.staticType = staticType;
    }

    public Class<?> type() {
        return staticType;
    }

    public Object value() {
        return value;
    }
}