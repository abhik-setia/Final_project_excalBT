package com.example.vinay.bluesample;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import static com.example.vinay.bluesample.TrackActivity.found;

public class MediaActivity extends AppCompatActivity {
    MediaPlayer media;
    NotificationManager notificationManager;
    AlertDialog al;

    private CameraManager mCameraManager;
    private String mCameraId;
    private Boolean isTorchOn,isVibrationOn;
    Handler vibration_handler;
    Runnable r;

    Boolean noti,alarm,flash,vibrate;
    SharedPreferences user_shared_pref;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);//builder is a static inner class in alert dialog
        builder.setTitle("Alert").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (media != null) media.stop();

                Intent i = new Intent(MediaActivity.this, DiscoveringActivity.class);
                startActivity(i);
                finish();
            }
        });

        user_shared_pref=getSharedPreferences("user_pref",Context.MODE_PRIVATE);
        noti=user_shared_pref.getBoolean("notification_preference",true);
        alarm=user_shared_pref.getBoolean("alarm_preference",true);
        flash=user_shared_pref.getBoolean("flash_preference",true);
        vibrate=user_shared_pref.getBoolean("vibration_preference",true);

        Log.v("hello",noti+" "+alarm+" "+flash+" "+vibrate);
        //to display a dialog box on pressing a button
        al = builder.create();
        al.setIcon(R.drawable.ic_error_black_24dp);
        al.setMessage("Device not found. Press Ok to stop the alarm");
        al.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                media = MediaPlayer.create(MediaActivity.this, R.raw.alert);
                media.setLooping(true);
                media.start();
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Intent intent = new Intent(MediaActivity.this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(MediaActivity.this, 1, intent, 0);

                //reqcode- anynumber
                if(noti==true) {
                    Notification notification1 = new Notification.Builder(MediaActivity.this).setSmallIcon(R.drawable.logo11).setContentText("Alert : Device out of range")
                            .setContentTitle("BT Tracker").setAutoCancel(true)
                            .setContentIntent(pendingIntent).build();
                    notificationManager.notify(1, notification1);
                }

            }

        });
        al.show();
        al.setCancelable(false);
        al.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                turnOffFlashLight();
                media.stop();
                vibration_handler.removeCallbacks(r);
            }
        });
        found = 0;
        isTorchOn = false;
        isVibrationOn=false;



        if(flash)
        start_flashlight();
        if(vibrate){
            start_vibration();
            Log.v("hello","started vibration");
        }

    }

    //fire up flashlight
    public void start_flashlight(){

        Boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    isTorchOn=true;
        if(isFlashAvailable){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                try {
                    mCameraId = mCameraManager.getCameraIdList()[0];
                    turnOnFlashLight();
                } catch (CameraAccessException e) {
                    e.printStackTrace();

                }
            }
        }
    }

    public void  start_vibration(){
        final Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        isVibrationOn=true;
        vibration_handler=new Handler();
        r=new Runnable() {
            @Override
            public void run() {
            v.vibrate(1000);
                if(isVibrationOn)
                vibration_handler.postDelayed(this,2000);
            }
        };
        vibration_handler.postDelayed(r,200);
    }

    public void turnOnFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
                isTorchOn=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOffFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
                isTorchOn=false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
//        if(isTorchOn){
//            turnOffFlashLight();
//        }
//        if(isVibrationOn)
//            vibration_handler.removeCallbacks(r);
        if (media != null)
            media.stop();

    }

    @Override
    protected void onPause() {
        super.onPause();
//        if(isTorchOn){
//            turnOffFlashLight();
//        }
//        if(isVibrationOn)
//            vibration_handler.removeCallbacks(r);
        if (media != null)
            media.stop();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(isTorchOn){
//            turnOnFlashLight();
//        }
//        if(!isVibrationOn)
//            start_vibration();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (media != null)
            media.stop();
        if(isTorchOn){
            turnOffFlashLight();
        }
        if(isVibrationOn)
            vibration_handler.removeCallbacks(r);

    }

}