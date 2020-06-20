package me.liwenkun.reflectutil;

public class Main {
    private static int a = 12;
    public static void main(String[] args) {
        String testString = "ReflectUtil";
        int hash = testString.hashCode();
        int reflectHash;
        try {
            reflectHash = Reflect.withInstance(testString, "hash", true);
            System.out.println(reflectHash == hash);

            System.out.println((boolean)Reflect.withStatic(Main.class, ":a.equals(1)",
                    true, 100, Values.wrappedAs(10, Object.class)));
            System.out.println(a);

            System.out.println((boolean)Reflect.withStatic(Main.class, "function(1).equals(1)",
                    true, Values.wrappedAs(1000, int.class), Values.wrappedAs(0, Object.class)));
            System.out.println((boolean)Reflect.withStatic(Main.class, "function(1).equals(1)",
                    true, Values.wrappedAs(1000, Integer.class), Values.wrappedAs(1, Object.class)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int function(int i) {
        return 0;
    }

    public static int function(Integer j) {
        return 1;
    }
}
