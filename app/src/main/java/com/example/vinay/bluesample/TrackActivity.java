package com.example.vinay.bluesample;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class  TrackActivity extends AppCompatActivity {
int initial;
    EditText ed;
    int thres;
    String add;
    String mydev;
    int count=0;
    Button b1,b2;
    BluetoothAdapter adapter;
    int flag=0, initflag=1;
    BroadcastReceiver find;
    MediaPlayer media;
    CanvasView canvas;
    int []  arr=new int[20];
    String [] arrS=new String[20];
    double init_dist;

   // int dist[]={10,66,74,80,84,88};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        ed=(EditText)findViewById(R.id.editText);
        b1=(Button)findViewById(R.id.button2);
        b2=(Button)findViewById(R.id.button3);
       //
       canvas=(CanvasView)findViewById(R.id.signature_canvas);
        Intent i=getIntent();
        //Toast.makeText(this, "Intent nhi mila", Toast.LENGTH_SHORT).show();

        initial=(i.getExtras().getInt("initial"));
        mydev=i.getExtras().getString("device");
        add=i.getExtras().getString("address");
        init_dist=calculate_distance(initial);
        Log.e("Initial dist=",""+init_dist);
        Toast.makeText(this, "Device: "+mydev+"\nAddress: "+add+"\nRssiinitial: "+initial, Toast.LENGTH_SHORT).show();
        adapter=BluetoothAdapter.getDefaultAdapter();




        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.isDiscovering())adapter.cancelDiscovery();
                ed.setEnabled(true);

                initflag=1;
                ed.setText("");

              //  if(find!=null)
                 //     unregisterReceiver(find);

            }
        });

    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Stop Tracking")
                .setMessage("Are you sure you want to stop Tracking?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
   /* public  void clean(View v)
    {
        canvas.clearCanvas();
    }*/
    public void scan_dev(final View v) {
        String e=ed.getText().toString();
        if(initflag==1 && (e.equals("")))
            Toast.makeText(TrackActivity.this, "Re-enter threshold distance", Toast.LENGTH_SHORT).show();

        else if(initflag==1 && !e.equals("") ) {

          //  b1.setEnabled(false);

            thres=Integer.parseInt(ed.getText().toString());
            ed.setEnabled(false);
            initflag=0;
            scan_dev(v);
          //  Intent i=new Intent(this,MediaActivity.class);

            //startActivity(i);  finish();
        }
        else{
          //  Toast.makeText(TrackActivity.this, "yippee", Toast.LENGTH_SHORT).show();
            if(find!=null)unregisterReceiver(find);

            find = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                   // Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                        //dialog.dismiss();
                        BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if(device!=null){
                            if(device.getAddress().equals(add)){

                                int rssi =intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                                double curr_dist=calculate_distance(rssi);
                                Log.e("Current addr=",""+curr_dist);
                               Toast.makeText(TrackActivity.this,""+rssi,Toast.LENGTH_SHORT).show();
                                arr[count]=rssi;
                                arrS[count]=new String(device.getName());
                                Log.e("Here :" , "" + rssi);
                               // if(Math.abs(rssi)>(Math.abs(initial)+thres)){
                                 if(curr_dist>init_dist+thres){
                                    flag=1;
                                    Log.e("here",rssi+" "+initial+" "+thres);
                                  //  Toast.makeText(context,rssi+" "+initial+" "+thres, Toast.LENGTH_SHORT).show();
                                    media= MediaPlayer.create(TrackActivity.this, R.raw.alert );
                                    media.start();

                                //   Toast.makeText(context, "bja na "+flag, Toast.LENGTH_SHORT).show();
                                    adapter.cancelDiscovery();
                                  // unregisterReceiver(find);
                                   //startActivity(new Intent(TrackActivity.this,MediaActivity.class));
                                    //finish();
                                   // if(flag==1)
                                      //  return;
                                }

                            }
                        }}


                    if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                      //  Toast.makeText(context, "Discovery khataam", Toast.LENGTH_SHORT).show();
                        Log.e("here","khatam");

                          if(flag==1){
                              startActivity(new Intent(TrackActivity.this,MediaActivity.class));
                              finish();
                          }
                       flag=0;
                       // canvas.setArr(arr,arrS,count);

                       scan_dev(v);
                    }
                    if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                      //  Toast.makeText(context, "Discovery shuru", Toast.LENGTH_SHORT).show();
                        Log.e("here","Shuru");
                        //  dialog.setMessage("Discovery started");
                        //dialog.show();
                    }

                }
            };
            IntentFilter filter = new IntentFilter();

            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            if (adapter != null) {
                if (adapter.isDiscovering()) {
                    adapter.cancelDiscovery();

                }
            }


            registerReceiver(find, filter);
            adapter.startDiscovery();
        }


    }


double calculate_distance(int rssi){
    double txPower = -66 ;//hard coded power value. Usually ranges between -59 to -65

    if (rssi == 0) {
        return -1.0;
    }

    double ratio = rssi*1.0/txPower;
    if (ratio < 1.0) {
        return Math.pow(ratio,10);
    }
    else {
        double distance =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
        return distance;
    }
}
    @Override
    protected void onDestroy() {
        if(find!=null)
        unregisterReceiver(find);
    if(media!=null)media.stop();
        super.onDestroy();

    }

}

