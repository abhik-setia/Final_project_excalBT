package com.example.vinay.bluesample;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MediaActivity extends AppCompatActivity {
MediaPlayer media;
    NotificationManager notificationManager;
    AlertDialog al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);


        AlertDialog.Builder builder=new AlertDialog.Builder(this);//builder is a static inner class in alert dialog
        builder.setTitle("Alert").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(media!=null)media.stop();

                Intent i=new Intent(MediaActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });



        //to display a dialog box on pressing a button
        al=builder.create();
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
                Notification notification1 = new Notification.Builder(MediaActivity.this).setSmallIcon(R.drawable.logo11).setContentText("Alert : Device out of range")
                        .setContentTitle("BT Gaurd").setAutoCancel(true)
                        .setContentIntent(pendingIntent).build();
                notificationManager.notify(1, notification1);


            }

        });
        al.show();

    }
}
