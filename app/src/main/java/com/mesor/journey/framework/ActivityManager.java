package com.mesor.journey.framework;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by Limeng on 2016/8/25.
 */
public class ActivityManager {
    private static ActivityManager activityManager;
    public Stack<Activity> activityStack;

    public static ActivityManager getInstance() {
        if(activityManager == null) {
            activityManager = new ActivityManager();
        }
        return activityManager;
    }

    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
            activityStack.push(activity);
        }
    }

    public Activity removeTopActivity() {
        if (activityStack.size() == 0) {
            return null;
        }
        return activityStack.pop();
    }

    public Activity getTopActivity() {
        if (activityStack.size() == 0) {
            return null;
        }
        return activityStack.peek();
    }
}
