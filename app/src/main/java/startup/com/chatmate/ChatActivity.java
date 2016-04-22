package startup.com.chatmate;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Harshil on 29/02/2016.
 */


public class ChatActivity extends AppCompatActivity{

    EditText et_msg;
    //FloatingActionButton fb_chat;
    List<ChatMessage> chat_array;
    ChatRecyclerAdapter adapter;
    RecyclerView lv;
    ImageView enter;
    int chat_active=0;
    boolean is_online=false;
    SharedPreferences pref;

    String today;
    String name,email,id,img_url;
    Calendar calendar;

    String URL = "http://chatmate.comlu.com/send_msg.php";

    private Socket mSocket;
    private RequestQueue mQueue;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    MessageDBHelper db;

    SharedPreferences dpref;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);
        //ChatApplication app = (ChatApplication) getApplication();
        //mSocket = app.getSocket();
        //mSocket.on(Socket.EVENT_CONNECT_ERROR,);
        //mSocket.on("newMsgRc", msgReceived);
        //mSocket.on("userConnect", userConnected);
        //mSocket.on("userDisconnect", userDisconnected);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id = getIntent().getExtras().getString("id");
        name = getIntent().getExtras().getString("name");
        email = getIntent().getExtras().getString("email");
        img_url = getIntent().getExtras().getString("img_url");

        setup();

    }

    public void setup(){
        lv = (RecyclerView) findViewById(R.id.rc_chat);


        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        ImageView iv_image = (ImageView) findViewById(R.id.iv_image);
        tv_name.setText(name);
        Picasso.with(this)
                .load(img_url)
                .into(iv_image);

        String tb_name="u_"+id;
        Log.i("TB",tb_name);

        dpref = getSharedPreferences(tb_name, 0);
        dpref.getBoolean("table_created",false);

        db = new MessageDBHelper(this,tb_name);



        et_msg = (EditText) findViewById(R.id.et_msg);

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();


        if(dpref.getBoolean("table_created",false)){
            chat_array = db.fetch_messages();
        }
        else{
            db.create_table(tb_name);
            editor = dpref.edit();
            editor.putBoolean("table_created",true);
            editor.apply();
        }

        if(chat_array==null){
            chat_array = new ArrayList<>();
        }

        adapter=new ChatRecyclerAdapter(this,chat_array,false);
        lv.setAdapter(adapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        //mLayoutManager.setReverseLayout(true);
        //mLayoutManager.setStackFromEnd(true);
        lv.setLayoutManager(mLayoutManager);
        enter = (ImageView) findViewById(R.id.enter_chat1);

        et_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.equals("")){
                    enter.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_send));
                    chat_active=0;

                }
                else{
                    chat_active=1;
                    enter.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_send_active));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_msg.getText().toString().equals("")){
                    enter.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_send));
                    chat_active=0;

                }
            }
        });
        pref = getSharedPreferences("Registration", 0);




        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = et_msg.getText().toString();
                if(!txt.equals("") && chat_active==1){
                    calendar = Calendar.getInstance();
                    int Hr24=calendar.get(Calendar.HOUR_OF_DAY);
                    int Min=calendar.get(Calendar.MINUTE);
                    String timestamp = Hr24+":"+Min;
                    ChatMessage msg = new ChatMessage();
                    msg.setMessageText(txt);
                    msg.setUserType(UserType.SELF);
                    msg.setMessageTime(timestamp);
                    msg.setMessageStatus(Status.SENT);
                    chat_array.add(msg);
                    adapter.animateTo(chat_array);
                    lv.scrollToPosition(chat_array.size() - 1);
                    et_msg.setText("");
                    enter.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_send));
                    chat_active=0;
                    volley_send(msg);

                }
            }
        });


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                calendar = Calendar.getInstance();
                int Hr24=calendar.get(Calendar.HOUR_OF_DAY);
                int Min=calendar.get(Calendar.MINUTE);
                String timestamp = Hr24+":"+Min;
                String message = intent.getStringExtra("data");
                String sender = intent.getStringExtra("name");
                if(sender.equals(email)){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setMessageText(message);
                    chatMessage.setMessageTime(timestamp);
                    chatMessage.setUserType(UserType.OTHER);
                    chat_array.add(chatMessage);
                    adapter.animateTo(chat_array);
                    lv.scrollToPosition(chat_array.size() - 1);
                    db.addMessage(chatMessage);
                }

            }
        };
    }

    public void volley_send(final ChatMessage message){
        String msg = message.getMessageText();

        String sender = pref.getString("Email","nope");
        String sendMsg = sender+" "+msg;
        JSONObject son = new JSONObject();
        try {
            son.put("email",email);
            son.put("sender",sender);
            son.put("message",sendMsg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jreq = new JsonObjectRequest(URL, son, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");
                    //String message = response.getString("message");
                    //Toast.makeText(ChatActivity.this,message,Toast.LENGTH_SHORT).show();
                    if(success==1){
                        work_done(message);
                    }
                    else{
                        volley_send(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        jreq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(jreq);

    }

    private void work_done(ChatMessage msg) {
        chat_array.remove(msg);
        adapter.animateTo(chat_array);
        msg.setMessageStatus(Status.DELIVERED);
        chat_array.add(msg);
        adapter.animateTo(chat_array);
        lv.scrollToPosition(0);
        lv.scrollToPosition(chat_array.size() - 1);
        db.addMessage(msg);


    }

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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;


            case R.id.menu_add_chat:
                calendar = Calendar.getInstance();
                ChatMessage msg = new ChatMessage();
                int Hr24=calendar.get(Calendar.HOUR_OF_DAY);
                int Min=calendar.get(Calendar.MINUTE);
                String timestamp = Hr24+":"+Min;
                msg.setMessageText("Hodor..!!");
                msg.setUserType(UserType.OTHER);
                msg.setMessageTime(timestamp);
                chat_array.add(msg);
                adapter.animateTo(chat_array);
                lv.scrollToPosition(chat_array.size() - 1);
                return true;

            case R.id.menu_clear_chat:
                if(db.TABLE_CREATED==1 || dpref.getBoolean("table_created",false)){
                    db.removeAll();
                    chat_array.clear();
                    adapter.animateTo(chat_array);
                    lv.scrollToPosition(0);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("data")) {
                //setContentView(R.layout.viewmain);
                // extract the extra-data in the Notification
                String msg = extras.getString("data");
                id = getIntent().getExtras().getString("id");
                name = getIntent().getExtras().getString("name");
                email = getIntent().getExtras().getString("email");
                img_url = getIntent().getExtras().getString("img_url");
                setup();
                String timestamp = getIntent().getExtras().getString("time");
                Log.i("newIntent", msg);
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessageText(msg);
                chatMessage.setMessageTime(timestamp);
                chatMessage.setUserType(UserType.OTHER);
                db.addMessage(chatMessage);
                chat_array.add(chatMessage);
                adapter.animateTo(chat_array);
                lv.scrollToPosition(chat_array.size() - 1);


                //tv1.setText(msg);
            }
        }
    }

    /*
    Emitter.Listener msgReceived = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                    @Override
                public void run() {
                    calendar = Calendar.getInstance();
                    int Hr24=calendar.get(Calendar.HOUR_OF_DAY);
                    int Min=calendar.get(Calendar.MINUTE);
                    String timestamp = Hr24+":"+Min;
                    JSONObject ob =(JSONObject)args[0];
                    ChatMessage msg = new ChatMessage();
                    try {
                        msg.setUserType(UserType.OTHER);
                        msg.setMessageText(ob.getString("messageText"));
                        msg.setMessageTime(timestamp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    chat_array.add(msg);
                    adapter.animateTo(chat_array);
                    lv.scrollToPosition(chat_array.size() - 1);

                }
            });
        }
    };

    Emitter.Listener userConnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };


    Emitter.Listener userDisconnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };
*/


}
