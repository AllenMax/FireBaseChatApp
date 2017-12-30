package com.example.allenrajumathew.firebasechatapp.UI;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allenrajumathew.firebasechatapp.MainActivity;
import com.example.allenrajumathew.firebasechatapp.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Allen Raju Mathew on 8/17/2017.
 */

public class QuickStartRecorder extends AppCompatActivity {

    //Set Android Fields

    public ImageView btnStopRec, btnBookmark, imgEditNotes;
    public TextView txtTimer;
    public TextView txtTimer_notes;
    public EditText editTextNotes;

    public ImageView btnDialogBookmark;
    public Button btnDialogSaveNotes;

    //Set MediaRecorder
    public MediaRecorder mediaRecorder;

    //Set Audio File Path Name
    String AudioSavePathInDevice = null;

    //Set Permissions
    public static final int RequestPermissionCode = 1;

    //Set BoookMark and Notes List
    ArrayList<Integer> list_bookmark;
    ArrayList<Integer> temp_list_bookmark;
    ArrayList<String> list_notes;

    //Set Count Up Timer
    private long startHTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int secs;
    int mins;

    //global variable to use in all methods
    int noteTime;

    //Set Context
    final Context context = this;

    //Set mediaRecorderFlag
    //To check if mediaplayer is still recordng when onBackPressed
    int mediaRecorderFlag = 0;

    //Set Ardrino Bluetooth

//    private BluetoothSocket btSocket = null;
//    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    ConnectedThread mConnectedThread;
//    boolean bool=true;
//    private BluetoothAdapter btAdapter = null;
//    ArrayList<String> temp_bluetooth_list_bookmark; //Originally it was temp_bookmark

    public static boolean createDir() {
        boolean ret = true;

        //**To change
        //File name from BookMarkFireBase_AudioFiles to BookMark_AudioFiles
        File file = new File(Environment.getExternalStorageDirectory(), "BookMarkFireBase_AudioFiles");

        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Audio folder");
                ret = false;
            }
        }
        return ret;
    }

//    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
//
//        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
//        //creates secure outgoing connecetion with BT device using UUID
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_start_recorder);

        //Set BlueTooth

//        temp_bluetooth_list_bookmark=new ArrayList<String>();
//        //Toast.makeText(context, "Intent Flag==> "+ getIntent().getExtras().getInt("flag"), Toast.LENGTH_SHORT).show();
//        try {
//            String info="HC-05 00:02:5B:00:A5:A6";
//            String address = info.substring(info.length() - 17);
//            btAdapter = BluetoothAdapter.getDefaultAdapter();
//            BluetoothDevice device = btAdapter.getRemoteDevice(address);
//            btSocket = createBluetoothSocket(device);
//            mConnectedThread = new ConnectedThread(btSocket);
//
//
//
//            btSocket.connect();
//            mConnectedThread.start();
//        }
//        catch (Exception e)
//        {
//            System.out.print(e);
//        }

        //Set Android Fields

        btnBookmark = (ImageView) findViewById(R.id.imgBookmark);
        btnStopRec = (ImageView) findViewById(R.id.imgStopRec);

        txtTimer = (TextView) findViewById(R.id.txtTimer);

        editTextNotes = (EditText) findViewById(R.id.editTextNotes);
        imgEditNotes = (ImageView) findViewById(R.id.imgEditNotes);

        list_bookmark = new ArrayList<Integer>();

        temp_list_bookmark = new ArrayList<Integer>();

        list_notes = new ArrayList<String>();

        btnStopRec.setEnabled(false);

        createDir();
        recording();

        //Set Note Editor

        editTextNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Make editTextNotes Fields in backgroung invisible
                editNotesVisibility(0);

                //Creating Edit Note Dialoge
                final Dialog dialog = new Dialog(context);

                dialog.setContentView(R.layout.activity_quick_start_recorder_dialoge);
                dialog.setTitle("Notes");

                int total = (secs * 1000) + (mins * 60000);
                noteTime = total - 3000;//Exact Time with 3 sec delay

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//Sets button above the keyboard

                //Set Dialoge Android Fields
                final EditText editTextDialogNotes = (EditText) dialog.findViewById(R.id.editNotesText);//cannot reassign value to final variable
                btnDialogBookmark = (ImageView) dialog.findViewById(R.id.imgBookmark_notes);
                txtTimer_notes = (TextView) dialog.findViewById(R.id.txtTimer_notes);
                btnDialogSaveNotes = (Button) dialog.findViewById(R.id.dialogButtonSave);


                btnDialogBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bookmark(0);
                    }
                });


                // if button is clicked, close the custom dialog
                btnDialogSaveNotes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list_bookmark.add(noteTime);
                        list_notes.add(editTextDialogNotes.getText().toString());

                        if (!temp_list_bookmark.isEmpty()) {
                            for (int i = 0; i < temp_list_bookmark.size(); i++) {
                                list_bookmark.add(temp_list_bookmark.get(i));
                                list_notes.add("");
                            }
                            temp_list_bookmark.clear();
                        }
                        dialog.dismiss();
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        editNotesVisibility(1);
                    }
                });
                dialog.show();
            }
        });

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmark(1);
            }
        });

        btnStopRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaRecorder.stop();
                mediaRecorderFlag = 1;//Used in case of onBackPressed()

                btnStopRec.setEnabled(false);

                //Check BlueTooth Bookmarks

//                bool=false;

//                if(!temp_bluetooth_list_bookmark.isEmpty())
//                {
//                    HashSet<String> uniqueValues = new HashSet<>(temp_bluetooth_list_bookmark);
//                    for (String value : uniqueValues) {
//                        list_bookmark.add(Integer.parseInt(value));
//                        list_notes.add("");
//                    }
//                }
//                Toast.makeText(getApplicationContext(),temp_bluetooth_list_bookmark.toString(),Toast.LENGTH_LONG).show();

                Toast.makeText(QuickStartRecorder.this, "Recording Completed", Toast.LENGTH_SHORT).show();

                boolean trial = true;

                //Flag
                ///////////////////////////////////////////////////////////////

                //Individual recorder
                if (trial) {   //if ( getIntent().getExtras().getInt("flag") == 0){

                    Uri uri = Uri.parse(AudioSavePathInDevice);

                    DateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
                    String today = formatter.format(new Date());

                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(getApplicationContext(), uri);
                    String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                    String query1 = "INSERT INTO tbl_RecordList ( RecordName, RecordDate, RecordDuration) " +
                            "VALUES ('" + AudioSavePathInDevice + "', '" + today + "', '" + duration + "')";

                    boolean verify_query = MainActivity.internalDBConnection.insertData(query1);

                    //To Check Query Statment
                    //**To Remove
                    Toast.makeText(QuickStartRecorder.this, "Inserting Recorging\n" + query1, Toast.LENGTH_SHORT).show();


                    //**To Change
                    //put query2 in below if statment
                    if (verify_query) {
                        Toast.makeText(QuickStartRecorder.this, "Inserted", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(QuickStartRecorder.this, "NOT Inserted", Toast.LENGTH_SHORT).show();
                    }

                    String query2 = "SELECT * FROM tbl_RecordList";
                    Cursor cursor = MainActivity.internalDBConnection.selectData(query2);

//                Set<Integer> hs = new HashSet<>();
//                hs.addAll(list_bookmark);
//                list_bookmark.clear();
//                list_bookmark.addAll(hs);

                    if (cursor.moveToLast()) {

                        Collections.sort(list_bookmark);

                        for (int i = 1; i < list_bookmark.size(); i++) {

                            if (list_bookmark.get(i) < 0) {
                                list_bookmark.set(i, 0);
                            }

                            //** To Change Remove .toString() in cursor.getString(0).toString()
                            String sss = "INSERT INTO tbl_Bookmark ( RecordID, StampTime,Notes) " +
                                    "VALUES ('" + cursor.getString(0).toString() + "','" + list_bookmark.get(i) + "', '" + list_notes.get(i) + "')";

                            verify_query = MainActivity.internalDBConnection.insertData(sss);

                        }
                        if (verify_query) {
                            Toast.makeText(QuickStartRecorder.this, "Inserting Bookmark\n" + list_bookmark.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(QuickStartRecorder.this, "Inserting Notes\n" + list_notes.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(QuickStartRecorder.this, "NOT Stamped", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {

                    //Organization Recorder recorder
                    Toast.makeText(context, "Organization Recorder recorder", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    public void recording() {

        if (checkPermission()) {

            //**To Change
            // Make noOfRows global and
            // AudioSavePathInDevice can be initialized in MediaRecorderReady()
            //Later
            //Find the total number of Recordings and add 1 to the new Recorging name
            String query1 = "Select * from tbl_RecordList";
            Cursor cursor = MainActivity.internalDBConnection.selectData(query1); //String query2 = "SELECT * FROM tbl_RecordList";
            int noOfRows = cursor.getCount() + 1;

            AudioSavePathInDevice =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            "BookMark_AudioFiles" + "/" + "AudioRecording " + noOfRows + ".3gp";

            MediaRecorderReady();

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                startHTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            btnStopRec.setEnabled(true);

            Toast.makeText(QuickStartRecorder.this, "Recording started", Toast.LENGTH_SHORT).show();

        } else {
            requestPermission();
        }
    }

    //Set VIsibility to EditText in background of Dialoge Box
    private void editNotesVisibility(int visibility) {

        int visibilityView = View.INVISIBLE;

        if (visibility == 1) {
            visibilityView = View.VISIBLE;
        }

        editTextNotes.setAlpha(visibility);
        imgEditNotes.setVisibility(visibilityView);
    }

    //Set Timer Runnable Thread
    public Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startHTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            secs = (int) (updatedTime / 1000);
            mins = secs / 60;
            secs = secs % 60;


            if (txtTimer != null)
                txtTimer.setText("" + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs));

            if (txtTimer_notes != null)
                txtTimer_notes.setText("" + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs));

            customHandler.postDelayed(this, 0);
        }
    };

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(QuickStartRecorder.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(QuickStartRecorder.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(QuickStartRecorder.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public void bookmark (int flag){

        int total = (secs * 1000) + (mins * 60000);

        String time_stamp = Integer.toString(total - 3000);//Exact Time with 3 sec delay

        if (flag == 1){
            list_bookmark.add(Integer.parseInt(time_stamp));
            list_notes.add("");
        }
        else{
            temp_list_bookmark.add(Integer.parseInt(time_stamp));
        }

        Toast.makeText(QuickStartRecorder.this, "Time Stamped",Toast.LENGTH_SHORT).show();
        //Toast.makeText(QuickStartRecorder.this, list_bookmark.toString(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaRecorderFlag == 0)
            mediaRecorder.stop();
    }

    //BlueTooth Funtion

    public class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

//        public void run() {
//            byte[] buffer = new byte[256];
//            int bytes;
//
//            // Keep looping to listen for received messages
//            while (bool) {
//                try {
//
//
//                    int total = (secs * 1000) + (mins * 60000);
//
//                    //Exact Time
//                    String string = Integer.toString(total);
//
//                    //Exact Time with 3 sec delay
//                    String string2 = Integer.toString(total - 3000);
//
//                    temp_bluetooth_list_bookmark.add(string2);
//
//                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
//                    String readMessage = new String(buffer, 0, bytes);
//                    Log.d("datas",readMessage);
//
//                    // Send the obtained bytes to the UI Activity via handler
//                    // bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
//                } catch (IOException e) {
//                    break;
//                }
//            }
//
//
//        }
//
//        //write method
//        public void write(String input) {
//            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
//            try {
//                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
//            } catch (IOException e) {
//                //if you cannot write, close the application
//                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
//                finish();
//
//            }
//        }


    }
}
