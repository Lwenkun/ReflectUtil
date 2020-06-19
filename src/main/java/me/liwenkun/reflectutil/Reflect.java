package me.liwenkun.reflectutil;

public class Reflect {

    // 静态开始的表达式执行
    public static <T> T withStatic(Class<?> cls, String expression, boolean recursive, Object... params) throws Exception {
        // noinspection unchecked
        return (T) Expression.create(cls, expression, recursive, params).evaluate(null, params);
    }

    public static <T> T withStatic(String cls, String expression, boolean recursive, Object... params) throws Exception {
        return withStatic(Class.forName(cls), expression, recursive, params);
    }

    // 实例开始的表达式执行
    public static <T> T withInstance(Object instance, String expression, boolean recursive, Object... params) throws Exception {
        // noinspection unchecked
        return (T) Expression.create(instance.getClass(), expression, recursive, params).evaluate(instance, params);
    }

}
