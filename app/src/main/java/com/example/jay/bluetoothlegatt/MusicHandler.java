package com.example.jay.bluetoothlegatt;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Jay on 2015-07-31.
 */
public class MusicHandler extends Handler {

    private static final int REQUEST_A = 1;

    private static final int REQUEST_B = 2;

    private static final int REQUEST_C = 3;

    private static final int REQUEST_D = 4;


    public void handleMessage(Message msg)
    {
        super.handleMessage(msg);

        switch (msg.what) {
            case REQUEST_A:


                break;


            default:

                break;
        }

    }
}
