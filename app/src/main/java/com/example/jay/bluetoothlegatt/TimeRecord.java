package com.example.jay.bluetoothlegatt;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Jay on 2015-07-31.
 */
public class TimeRecord {

    private DeviceControlActivity.MusicHandler mHandler;
    private TimerTask mTimer;
    int time_sec = 0;
    private static final int NOMAL_MODE = 5;
    private static final int RECORD_MODE = 6;
    private static final int PLAY_MODE = 7;
    private int temp_arr[];

    public TimeRecord(DeviceControlActivity.MusicHandler mHandler)
    {
        this.mHandler=mHandler;
    }

    public void record_start(){

        mHandler.sendEmptyMessage(RECORD_MODE);

        time_sec = 0;

        mTimer = new TimerTask() {

            public void run() {

                if(time_sec==300 || DeviceControlActivity.state==NOMAL_MODE)
                {
                    cancel_timer();
                }

                time_sec++;
            }
        };

        Timer timer = new Timer();
        timer.schedule(mTimer, 0, 80);
    }

    public int get_time()
    {
        return time_sec;
    }

    public void cancel_timer()
    {
        mHandler.sendEmptyMessage(NOMAL_MODE);
        DeviceControlActivity.state = NOMAL_MODE;
        time_sec = 0;
        mTimer.cancel();
    }

    public void Play_Recorded(final int temp_arr[])
    {
        mHandler.sendEmptyMessage(PLAY_MODE);

       this.temp_arr=temp_arr;

        time_sec = 0;

        mTimer = new TimerTask() {

           public void run() {

                if(temp_arr[time_sec]!=0)
                {
                    Log.d("Record Play",Integer.toString(time_sec));
                    mHandler.sendEmptyMessage(temp_arr[time_sec]);
                }

                if(time_sec==300 || DeviceControlActivity.state==NOMAL_MODE)
                {
                    cancel_timer();
                }

                time_sec++;
            }
        };

        Timer timer = new Timer();
        timer.schedule(mTimer, 0, 80);
    }

}
