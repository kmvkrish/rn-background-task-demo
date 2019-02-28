package com.backgroundtaskdemo.backgroundtask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.backgroundtaskdemo.MainActivity;
import com.backgroundtaskdemo.R;
import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

public class DemoHeadlessTaskService extends HeadlessJsTaskService {

    private static final String TAG = DemoHeadlessTaskService.class.getSimpleName();
    private static final int Notification_Id = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Log.d(TAG, String.format("%s running on Android Oreo", getResources().getString(R.string.app_name)));

            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, mainActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            Notification notification = new NotificationCompat.Builder(this, "backgroundtask")
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(
                            String.format(getResources().getString(R.string.app_running_background), getResources().getString(R.string.app_name))
                    ).setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.mipmap.ic_launcher).build();

            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            try {
                notificationManager.notify(Notification_Id, notification);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
            startForeground(Notification_Id, notification);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    protected HeadlessJsTaskConfig getTaskConfig(Intent intent) {

        Log.d(TAG, "BackgroundTask HeadlessJSTask service");

        Bundle extras = intent.getExtras();

        WritableMap data = extras != null ? Arguments.fromBundle(extras) : Arguments.createMap();

        if (extras != null) {
            int timeout = extras.getInt("timeout");
            Log.d(TAG, String.format("Returning HeadlessJsTaskConfig, timeout=%s ms", timeout));
            return new HeadlessJsTaskConfig(
                    "BackgroundTask",
                    data,
                    TimeUnit.SECONDS.toMillis(timeout),
                    false); //false is not allowing HeadlessService running in foreground.
        }

        return super.getTaskConfig(intent);
    }
}
