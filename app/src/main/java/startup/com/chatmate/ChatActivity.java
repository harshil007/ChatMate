package startup.com.chatmate;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

    String today;
    Calendar calendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lv = (RecyclerView) findViewById(R.id.rc_chat);



        String name = getIntent().getExtras().getString("name");
        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(name);

        et_msg = (EditText) findViewById(R.id.et_msg);



        chat_array = new ArrayList<>();

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

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
