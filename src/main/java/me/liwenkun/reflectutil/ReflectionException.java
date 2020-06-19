package me.liwenkun.reflectutil;

public class ReflectionException extends Exception {

    ReflectionException(String message) {
        super(message);
    }

    ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
