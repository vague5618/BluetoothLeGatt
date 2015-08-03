package com.example.jay.bluetoothlegatt;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by 재용 on 2015-08-01.
 */
public class HttpURLConnection extends Thread implements Runnable {

    private String mUrlStr;
    private static final String TAG ="HttpConnection";


    public HttpURLConnection(String mUrlStr) {
        this.mUrlStr = mUrlStr;
    }

    private String request() {
        StringBuilder output = new StringBuilder();

        try{

            HttpClient client= new DefaultHttpClient();

            ArrayList<NameValuePair> nameValuePairs =
                    new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("name", "유재석"));

            Log.e("TAG", " 1 ");

            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            Log.e("TAG", " 2 ");
            UrlEncodedFormEntity entityRequest =
                    new UrlEncodedFormEntity(nameValuePairs, "EUC-KR");

            HttpPost httppost = new HttpPost(mUrlStr);

            httppost.setEntity(entityRequest);

            Log.e("TAG", " 3 ");

            HttpResponse response = client.execute(httppost);

            Log.e("TAG", " 4 ");

            InputStream instream = response.getEntity().getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(instream));

            output.append(reader);

            Log.e("TAG", " Read : " + output.toString()) ;

        }
        catch (Exception e)
        {
            Log.e("TAG", " Fail ") ;
            e.printStackTrace();
        }

        return output.toString();
    }

    @Override
    public void run() {
        super.run();

        while(true) {

            request();
        }
    }
}
