package me.liwenkun.reflectutil;

import org.jetbrains.annotations.NotNull;

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
}
