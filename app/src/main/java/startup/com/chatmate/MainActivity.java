package startup.com.chatmate;


import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    ListView list;
    ImageView iv_search;
    TextView title;
    EditText et_search;
    int flag = 0;
    Menu mn;
    private RequestQueue mQueue;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public ViewPager viewPager;
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "MainAcrivity";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private static final String LOGIN_URL = "http://chatmate.comlu.com/register_user.php";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String name,email,img_url,token;
    ProgressDialog pDialog;
    PagerAdapter adapter;
    private static final String SENDER = "795935506721";

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,new IntentFilter("pushmsg"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = getSharedPreferences("Registration", 0);
        name = pref.getString("Name","Emma Watson.!");
        email= pref.getString("Email","emma.watson@harshil.com");
        img_url = pref.getString("Pic_url","R.drawable.emma_watson");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Connecting to Server...");
        pDialog.setCancelable(false);
        pDialog.show();

        registerGCM();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("OFFLINE"));
        tabLayout.addTab(tabLayout.newTab().setText("CHATS"));
        tabLayout.addTab(tabLayout.newTab().setText("CONTACTS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayout.setSelectedTabIndicatorHeight(5);

        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                onCreateOptionsMenu(mn);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        list = (ListView)findViewById(R.id.chat_list);

    }

    public void update_chat_list(UserModel user){
        TabFragment2 tab2 =(TabFragment2) adapter.getFragment(1);
        tab2.update_list(user);
    }

    private void registerGCM() {
        // [START get_token]

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {

                    InstanceID instanceID = InstanceID.getInstance(MainActivity.this);
                    try {
                        token = instanceID.getToken(SENDER,
                                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // [END get_token]

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.i(TAG, "GCM Registration Token: " + token);
                editor = pref.edit();
                editor.putString("token",token);
                editor.apply();
                volleyRegister();

            }
        }.execute();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void volleyRegister() {
        JSONObject son = new JSONObject();
        try {
            son.put("email", email);
            son.put("name", name);
            son.put("token",token);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jreq = new JsonObjectRequest(LOGIN_URL, son, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                pDialog.dismiss();
                try {
                    int i = jsonObject.getInt("success");
                    String msg = jsonObject.getString("message");

                    switch (i){
                        case 1:
                            Toast.makeText(MainActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                volleyError.printStackTrace();
                Toast.makeText(MainActivity.this,"Connection Error! Try Again!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        jreq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(jreq);


    }

}
