package com.backgroundtaskdemo;

import com.backgroundtaskdemo.backgroundtask.DemoJob;
import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "BackgroundTaskDemo";
    }

    @Override
    protected void onResume() {
        super.onResume();
        DemoJob.cancelTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DemoJob.schedulePeriodicJob();
    }
}
