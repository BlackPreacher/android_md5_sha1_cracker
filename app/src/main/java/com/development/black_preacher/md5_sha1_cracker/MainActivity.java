package com.development.black_preacher.md5_sha1_cracker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

        Button subscribeButton = (Button) findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("news");
                Log.i(TAG, "Subscribed to news topic");
                // [END subscribe_topics]
            }
        });

        Button logTokenButton = (Button) findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());
            }
        });


    }

    public void calculate_plain(View v) {

        //String linkip = "http://[2a02:908:dc41:200:b1b7:6ec9:7164:e96e]";
        String linkip = "http://blackpreacher.bplaced.net/";

        String hash = "";

        EditText ed_hash = (EditText)findViewById(R.id.hash_edit);
        TextView result_view = (TextView) findViewById(R.id.result_view);
        hash = ed_hash.getText().toString();

        String result = "";
        try {
            HashMap<String,String> tmp = new HashMap<>();
            tmp.put("url",linkip+"/tools/solve_plain.php");
            tmp.put("hash",hash);
            result = new NetworkOperations().execute(tmp).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final JSONArray jArray_ein;
        try {
            jArray_ein = new JSONArray(result);

            final int laenge_ein = jArray_ein.length();

            Log.i("Main",Integer.toString(laenge_ein));
            if(laenge_ein > 0) {
                for (int i = 0; i < laenge_ein; i++) {
                    JSONObject data;
                    data = jArray_ein.getJSONObject(i);
                    String plain = data.getString("plain");
                    result_view.setText("The password you searched is: " + plain);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(result_view.getWindowToken(),0);
                }
            } else {
                result_view.setText("Sorry, I cant crack this password");
            }

        } catch (JSONException e) {

            if(result.equals("no_connection")){
                result_view.setText("Sorry, but I need connection to the Server");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(result_view.getWindowToken(),0);
            } else {
                e.printStackTrace();
            }
        }

    }


}
