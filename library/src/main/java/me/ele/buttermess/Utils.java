package me.ele.buttermess;

import android.content.Context;

import java.lang.reflect.Method;

class Utils {

    private static Context CONTEXT;

    public static Context getApplicationContext() {
        if (CONTEXT != null) {
            return CONTEXT;
        } else {
            try {
                Class ignored = Class.forName("android.app.ActivityThread");
                Method method = ignored.getMethod("currentApplication", new Class[0]);
                CONTEXT = (Context) method.invoke(null);
                return CONTEXT;
            } catch (Exception var2) {
                return null;
            }
        }
    }


    public static String getPackageName() {
        return getApplicationContext().getPackageName();
    }
}
