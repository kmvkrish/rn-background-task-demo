package com.backgroundtaskdemo.backgroundtask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class DemoJobCreator implements JobCreator {

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case DemoJob.TASK:
                return new DemoJob();
            default: return null;
        }
    }
}
