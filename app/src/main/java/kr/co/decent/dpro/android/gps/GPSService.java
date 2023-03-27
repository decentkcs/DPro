package kr.co.decent.dpro.android.gps;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Timer;
import java.util.TimerTask;

import kr.co.decent.dpro.android.MainActivity;
import kr.co.decent.dpro.android.R;
import kr.co.decent.dpro.android.common.CommonConstants;

/**
 * Created by deepshikha on 24/11/16.
 */

public class GPSService extends Service implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;

    LocationManager locationManager;

    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 10000;
    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;
    Context mContext;

    public GPSService() {

    }

    public GPSService(Context context) {
        mContext = context;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StartForeground();
        if(intent == null)
            return Service.START_STICKY;
        else
        {
            if(mContext == null)
                mContext = getApplicationContext();

            notify_interval = intent.getLongExtra("interval", 10000);

            if(mTimer == null)
                mTimer = new Timer();
            else {
                mTimer.cancel();
                mTimer = new Timer();
            }

            mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);
            intent = new Intent(str_receiver);
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if(mTimer != null)
            mTimer.cancel();
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private void StartForeground() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String CHANNEL_ID = "channel_location";
        String CHANNEL_NAME = "channel_location";

        NotificationCompat.Builder builder = null;
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
            builder.setChannelId(CHANNEL_ID);
            builder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        }

        builder.setContentTitle("위치정보 사용 중");
        builder.setContentText(CommonConstants.PROJECT_NAME + "에서 위치정보를 사용하고 있습니다.");
        Uri notificationSound = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(notificationSound);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.icon);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(101, notification);
    }

    public Location getlocation(boolean isSingle) {

        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        double latitude=0, longitude=0;
        Location location = null;

        if (!isGPSEnable && !isNetworkEnable) {

        } else {
            if (isNetworkEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    Location location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location1 != null) {
                        latitude = location1.getLatitude();
                        longitude = location1.getLongitude();
                        location = location1;
                    }
                }
            }

            if (isGPSEnable) {

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    Location location2 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location2!=null){
                        latitude = location2.getLatitude();
                        longitude = location2.getLongitude();
                        location = location2;
                    }
                }
            }
            if(!isSingle) {

                if(latitude == 0 || longitude == 0) {
                }
                else {
                    sendMessage(latitude, longitude);
                }
            }
            return location;
        }
        return null;
    }

    private class TimerTaskToGetLocation extends TimerTask{
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    getlocation(false);
                }
            });

        }
    }

    private void sendMessage(double latitude, double longitude){
        Log.d("messageService", "Broadcasting message");
        Intent intent = new Intent("custom-event-name");
        intent.putExtra("latitude",latitude+"");
        intent.putExtra("longitude",longitude+"");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}