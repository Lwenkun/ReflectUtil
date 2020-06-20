package me.liwenkun.reflectutil;

public class Values {
    public static Object wrappedAs(Object value, Class<?> desiredType) {
        return new StaticTypeValue(value, desiredType);
    }
}
