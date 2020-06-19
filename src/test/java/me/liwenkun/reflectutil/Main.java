package me.liwenkun.reflectutil;

public class Main {
    public static void main(String[] args) {
        String testString = "ReflectUtil";
        int hash = testString.hashCode();
        int reflectHash = -1;
        try {
            reflectHash = Reflect.withInstance(testString, "hash", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(reflectHash == hash);
    }
}
