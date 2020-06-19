# ReflectUtil
A simple reflect util. You can access non-public methods and feilds in a simple way which can improve code readability in the same time. This can be useful when we try to hook the system.
For example, you can listen to the communication between AMS and application like this :

```java
Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
sCurrentActivityThreadField.setAccessible(true);
Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);
Field mHField = activityThreadClass.getDeclaredField("mH");
mHField.setAccessible(true);
final Handler mH = (Handler) mHField.get(sCurrentActivityThread);
Field mCallbackField = Handler.class.getDeclaredField("mCallback");
mCallbackField.setAccessible(true);
mCallbackField.set(mH, new Handler.Callback() {
    @Override
    public boolean handleMessage(Message msg) {
        doYourWork();
        return false;
    }
});
```
            
The same thing can be done in a simpler way by using this util:

```java
Reflect.withStatic("android.app.ActivityThread", "sCurrentActivityThread.mH.:mCallback", true, new Handler.Callback() {
            @Override
            public booelan handleMessage() {
                doYourWork();
                return false;
            }
});
```

A field starting with `:` means it want to replaced by the new Object as provided.

You can also use method in the expression:

```java
Reflect.withStatic("android.app.ActivityThread", "sCurrentActivityThread.mH.:mCallback.getClass().equals(1)", true, new Handler.Callback() {
            @Override
            public booelan handleMessage() {
                doYourWork();
                return false;
            }
}, Cast.of(Handler.Callback.class, Object.class));
```
`getClass()` returns the class representing the anonymous class we create. Then we call its `equals()` method with the param `Handler.Callback.class` as provided. In order to find the method `equals`, we have to point out the declaring type of it's param. We use `Cast.of` to do this. The first param we pass to it will be used when calling `equals` while the second indicates the declaring type of it's param, which is `Object` actually. The number inside the brackets indicates param count of `equals()`.

If you run the code above, you will get the result `false` as expected because `Handler.Callback.class` does not equal the class object representing its anonymous sub class.

