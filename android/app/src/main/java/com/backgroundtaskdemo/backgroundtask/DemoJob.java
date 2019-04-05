package com.backgroundtaskdemo.backgroundtask;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import java.util.concurrent.TimeUnit;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.facebook.react.HeadlessJsTaskService;

public class DemoJob extends Job {

    public static final String TASK = "BackgroundTask";

    private static JobRequest mJobRequest;

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {

        Context context = getContext().getApplicationContext();
        
        Intent headlessIntent = new Intent(context, DemoHeadlessTaskService.class);
        headlessIntent.putExtra("data", "starting background task");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Since Android Oreo, every background service must be started as foreground service.
            context.startForegroundService(headlessIntent);
        } else {
            context.startService(headlessIntent);
        }
        HeadlessJsTaskService.acquireWakeLockNow(context);

        return Result.SUCCESS;
    }

    public static void schedulePeriodicJob() {
        mJobRequest = new JobRequest.Builder(TASK)
                .setPeriodic(
                        TimeUnit.SECONDS.toMillis(900),
                        TimeUnit.SECONDS.toMillis(300)
                  )
                .build();

        mJobRequest.schedule();
    }

    public static void cancelTask() {
        if (mJobRequest != null) {
            JobManager.instance().cancel(mJobRequest.getJobId());
        }
    }
}
