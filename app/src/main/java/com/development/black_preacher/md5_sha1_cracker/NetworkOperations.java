package com.development.black_preacher.md5_sha1_cracker;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by bro on 26.05.2016.
 */
public class NetworkOperations extends AsyncTask<HashMap<String,String>,Void,String> {


    @Override
    protected String doInBackground(HashMap<String,String>... params) {


        Set<String> value = params[0].keySet();
        Collection<String> values2 = params[0].values();
        String url = params[0].get("url");
        String param = "";

        Iterator<String> it2 = values2.iterator();

        Iterator<String> it = value.iterator();

        if(it.hasNext()){
            String key = it.next();
            String key_value = it2.next();
            if(!key.equals("url"))
                try {
                    param = key + "=" + URLEncoder.encode(key_value,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
        }
        while(it.hasNext()){
            String key = it.next();
            String key_value = it2.next();
            if (!key.equals("url")){
                Log.i("iterator test", key+ " : " + key_value);
                try {
                    param = param + "&" + key + "=" + URLEncoder.encode(key_value,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }


        Log.i("PostString",param);
        Log.i("PostString size", Integer.toString(value.size()));

        final String postParameters = param;
        Log.i("Nwo","try to open Post with " + postParameters);

        if(value.size() < 2){
            return openURL(url);
        } else {
            Log.i("Nwo","open Post with " + postParameters);
            return openURLPost(url,postParameters);
        }

        //return null;
    }

    private String openURL(final String url){
        try{

            URL url_ein = new URL(url);

            HttpURLConnection conn_ein = (HttpURLConnection)url_ein.openConnection();

            BufferedReader in_ein = new BufferedReader( new InputStreamReader( conn_ein.getInputStream()));

            StringBuilder sb_ein = new StringBuilder();

            String tmp_ein = "";
            String line = "";
            sb_ein.setLength(0);

            if(in_ein != null)
            {
                while((line = in_ein.readLine()) != null)
                {
                    sb_ein.append(line+"\n");
                }
            }

            final String result = new String(sb_ein.toString());
            Log.i("Network","Result: " + result);

            return result;

        } catch ( IOException ex) {
            Log.i("IOException","IOException while loading URL: "+ ex);
            return "error";
        }


    }

    private String openURLPost(String str_url, String lparams){

        try {
            URL url = new URL(str_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String postParam = lparams;

            Log.i("NwO URLPOst", postParam);

            conn.setFixedLengthStreamingMode(postParam.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStream os = conn.getOutputStream();
            os.write(postParam.getBytes());
            os.flush();

            BufferedReader in_ein = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb_ein = new StringBuilder();

            String tmp_ein = "";
            String line = "";
            sb_ein.setLength(0);

            if (in_ein != null) {
                while ((line = in_ein.readLine()) != null) {
                    sb_ein.append(line + "\n");
                }
            }

            final String result = new String(sb_ein.toString());
            Log.i("Network","Result: " + result);

            return result;
        } catch ( IOException ex) {
            Log.i("IOException","IOException while loading URL: "+ ex);
            return "no_connection";
        }

    }

}
