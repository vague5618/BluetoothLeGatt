package com.example.jay.bluetoothlegatt;

/**
 * Created by Jay on 2015-07-31.
 */

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bluetoothlegatt.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity implements View.OnClickListener {

    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private static final int MUSIC_START = 0;
    private static final int REQUEST_A = 'a';
    private static final int REQUEST_B = 'b';
    private static final int REQUEST_C = 'c';
    private static final int REQUEST_D = 'd';
    private static final int NOMAL_MODE = 5;
    private static final int RECORD_MODE = 6;
    private static final int PLAY_MODE = 7;


    static public int state = NOMAL_MODE;
    public Context mContext = this;
    private MusicHandler mhandler;
    private MusicPlayerThread mMusicPlayerThread;
    private Button mButtonSetup;
    private Button mButtonWrite;
    private Button mButtonPlay;
    private TextView mTextview;
    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothThread mBluetootThread;
    private TimeRecord mTimeRecord;
    public int record_arr[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        mhandler = new MusicHandler();
        mhandler.sendEmptyMessage(MUSIC_START);
        mTimeRecord = new TimeRecord(mhandler);

        mBluetootThread = new BluetoothThread(this, mDeviceAddress, mDeviceName, mhandler);
        mBluetootThread.run();

        record_arr = new int[1500];

        mTextview = (TextView) findViewById(R.id.state);
        mButtonPlay = (Button) findViewById( R.id.button_c);
        mButtonPlay.setOnClickListener(this);
        mButtonPlay.setClickable(false);
        mButtonSetup = (Button) findViewById(R.id.button_a);
        mButtonSetup.setOnClickListener(this);
        mButtonSetup.setText("Timer start");
        mButtonWrite = (Button) findViewById(R.id.button_b);
        mButtonWrite.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBluetootThread.Thread_onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mBluetootThread.Thread_onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetootThread.Thread_onDestroy();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_a:

                if (state == NOMAL_MODE) {

                    for(int i=0; i<record_arr.length; i++)
                        record_arr[i] = 0;

                    state = RECORD_MODE;
                }

                mTimeRecord.record_start();

                break;

            case R.id.button_b:

                mBluetootThread.write(null);

                break;

            case R.id.button_c:

                if (state == NOMAL_MODE) {

                    state = PLAY_MODE;

                    mTimeRecord.Play_Recorded(record_arr);
                }

                break;

        }
    }

    class MusicHandler extends Handler {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MUSIC_START:

                    mMusicPlayerThread = new MusicPlayerThread(mContext);
                    break;

                case REQUEST_A:

                    mMusicPlayerThread.play(1);
                    if (state == RECORD_MODE) {

                        Log.d("Record",Integer.toString(mTimeRecord.get_time()) + "A" );

                        record_arr[mTimeRecord.get_time()] = REQUEST_A;
                    }

                    break;

                case REQUEST_B:

                    mMusicPlayerThread.play(2);

                    if (state == RECORD_MODE) {

                        Log.d("Record",Integer.toString(mTimeRecord.get_time()) + "B" );

                        record_arr[mTimeRecord.get_time()] = REQUEST_B;
                    }

                    break;


                case REQUEST_C:

                    mMusicPlayerThread.play(3);

                    if (state == RECORD_MODE) {

                        Log.d("Record",Integer.toString(mTimeRecord.get_time()) + "C" );

                        record_arr[mTimeRecord.get_time()] = REQUEST_C;
                    }

                    break;


                case REQUEST_D:

                    mMusicPlayerThread.play(4);

                    if (state == RECORD_MODE) {

                        Log.d("Record",Integer.toString(mTimeRecord.get_time()) + "D" );

                        record_arr[mTimeRecord.get_time()] = REQUEST_D;
                    }
                    break;


                case NOMAL_MODE:

                    mTextview.setText("no Record");
                    mButtonSetup.setClickable(true);
                    break;

                case RECORD_MODE:

                    mTextview.setText("Recording");
                    mButtonPlay.setClickable(true);
                    break;

                case PLAY_MODE:

                    mTextview.setText("Playing");
                    mButtonSetup.setClickable(false);
                    break;

                default:

                    break;
            }

        }
    }

    ;

}