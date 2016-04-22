package startup.com.chatmate;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;


/**
 * Created by Harshil on 18/04/2016.
 */
public class ProfileActivity extends AppCompatActivity {

    ImageView iv_profile;
    TextView tv_name,tv_email;
    Button b_okay;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String name,email,img_url,id;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        pref = getSharedPreferences("Registration", 0);

        id=pref.getString("ID","49");
        name = pref.getString("Name","Emma Watson.!");
        email= pref.getString("Email","emma.watson@harshil.com");
        img_url = pref.getString("Pic_url","R.drawable.emma_watson");

        iv_profile = (ImageView) findViewById(R.id.iv_profile_picture);

        tv_name = (TextView) findViewById(R.id.tv_profile_name);
        tv_name.setText(name);
        tv_email = (TextView) findViewById(R.id.tv_profile_email);
        tv_email.setText(email);
        b_okay = (Button) findViewById(R.id.b_okay);

        b_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        Picasso.with(this)
                .load(img_url)
                .placeholder(R.drawable.load)
                //.priority(Picasso.Priority.HIGH)
                .into(iv_profile);
        /*
        Glide.with(this)
                .load(img_url)
                .placeholder(R.drawable.load)
                .thumbnail( 0.1f )
                .crossFade()
                .into(iv_profile);*/
    }
}
