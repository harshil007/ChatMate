package startup.com.chatmate;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.wire.Wire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Harshil on 05/04/2016.
 */
public class GroupChatActivity extends AppCompatActivity{
    EditText et_msg;

    // DEFAULT IP
    public static String SERVERIP = "10.0.2.15";

    // DESIGNATE A PORT
    public static final int SERVERPORT = 8080;

    private Handler handler = new Handler();


    List<ChatMessage> chat_array;
    ChatRecyclerAdapter adapter;
    RecyclerView lv;
    ImageView enter;
    int chat_active=0;
    CircularImageView iv;

    ProgressDialog pDialog;

    String today;
    Calendar calendar;

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        String type = getIntent().getExtras().getString("type");
        if(type.equals("SERVER")){

        }
        else{

        }

        /*
        startup.com.chatmate.startup.com.chatmate.chatmsg msg = new startup.com.chatmate.startup.com.chatmate.chatmsg.Builder()
                .msgtext("Hello")
                .username("Harshil")
                .timestamp("Today")
                .build();

        byte[] data = startup.com.chatmate.startup.com.chatmate.chatmsg.ADAPTER.encode(msg);

        try {
            startup.com.chatmate.startup.com.chatmate.chatmsg msg2 = startup.com.chatmate.startup.com.chatmate.chatmsg.ADAPTER.decode(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lv = (RecyclerView) findViewById(R.id.rc_chat);
        iv = (CircularImageView) findViewById(R.id.imageview);
        iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_black_36dp));

        String name = getIntent().getExtras().getString("name");
        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(name);

        et_msg = (EditText) findViewById(R.id.et_msg);



        chat_array = new ArrayList<>();

        adapter=new ChatRecyclerAdapter(this,chat_array,true);
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
                    chat_array.add(msg);
                    adapter.animateTo(chat_array);
                    lv.scrollToPosition(chat_array.size() - 1);
                    et_msg.setText("");
                    enter.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_send));
                    chat_active=0;
                }
            }
        });

    }






    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public void receivedmsg(ChatMessage msg){
        chat_array.add(msg);
        adapter.animateTo(chat_array);
        lv.scrollToPosition(chat_array.size() - 1);
    }


}
