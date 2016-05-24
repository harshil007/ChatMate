package startup.com.chatmate;


import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;


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
    String name,email,img_url,token,id,image_url;
    ProgressDialog pDialog;
    PagerAdapter adapter;
    private static final String SENDER = "795935506721";
    ChatDBHelper chatDBHelper;

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
        id=pref.getString("ID","49");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Connecting to Server...");
        pDialog.setCancelable(false);
        pDialog.show();

        registerGCM();

        chatDBHelper = new ChatDBHelper(this);

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
        chatDBHelper.deleteChat(user);
        chatDBHelper.addContact(user);
        TabFragment2 tb = (TabFragment2)adapter.getFragment(1);
        tb.fetch_chats();
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
                uploadImage();
                volleyRegister();

            }
        }.execute();



    }

    private void uploadImage() {

        String name="u_"+id+".png";
        new FTP().execute(img_url,name);
        image_url = "http://chatmate.comlu.com/Images/profile_pics/"+name;
        editor = pref.edit();
        editor.putString("Pic_url",image_url);
        editor.apply();
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
            son.put("id",id);
            son.put("image_url",image_url);

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



    class FTP extends AsyncTask<Object,Void,Void> {


        @Override
        protected Void doInBackground(Object... params) {
            String src = (String) params[0];
            InputStream input=null;
            java.net.URL url = null;
            //InputStream input = (InputStream) params[1];
            try {
                url = new java.net.URL(src);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String name = (String) params[1];
            FTPClient ftp = new FTPClient();

            if(input==null){
                return null;
            }

            try {
                ftp.connect("chatmate.comlu.com");
                if(!ftp.login("a5619188", "WhySoSerious7"))
                {
                    ftp.logout();
                    return null;
                }
                int reply = ftp.getReplyCode();
                //FTPReply stores a set of constants for FTP reply codes.
                if (!FTPReply.isPositiveCompletion(reply))
                {
                    ftp.disconnect();
                    return null;
                }
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();
                ftp.changeWorkingDirectory("/public_html/Images/profile_pics");
                ftp.storeFile(name,input);
                ftp.logout();
                ftp.disconnect();


            } catch (IOException e) {
                e.printStackTrace();

            }

            return null;
        }
    }


}
