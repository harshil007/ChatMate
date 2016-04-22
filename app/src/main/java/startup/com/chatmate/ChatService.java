package startup.com.chatmate;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Harshil on 18/04/2016.
 */
public class ChatService extends GcmListenerService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String Sender;
    DBhandler db;
    MessageDBHelper db1;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        db = new DBhandler(this);


        //Here is called even app is not running.
        //create your notification here.
        Log.i("Sender",from);
        String msg = data.getString("message");
        //Toast.makeText(ChatService.this, msg, Toast.LENGTH_SHORT).show();
        if (msg != null) {
            int index = msg.indexOf(" ");
            if(index!=-1)
                Sender = msg.substring(0,index);
            msg = msg.substring(msg.indexOf(" ")+1,msg.length());
        }
        sendNotification(from, msg);


    }


    private void sendNotification(String sender,String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Calendar calendar = Calendar.getInstance();
        int Hr24=calendar.get(Calendar.HOUR_OF_DAY);
        int Min=calendar.get(Calendar.MINUTE);
        String timestamp = Hr24+":"+Min;

        Contact contact = db.getContact(Sender);
        String name = contact.getName();
        String email = contact.getEmail();
        String id = contact.getID();
        String img_url = contact.getImg_url();

        //db1 = new MessageDBHelper(this,id);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageText(msg);
        chatMessage.setMessageTime(timestamp);
        chatMessage.setUserType(UserType.OTHER);
        //db1.addMessage(chatMessage);

        Intent intent = new Intent(this, ChatActivity.class)
                //.addFlags(PendingIntent.FLAG_UPDATE_CURRENT)
                .putExtra("data", msg)
                .putExtra("name",name)
                .putExtra("id",id)
                .putExtra("img_url",img_url)
                .putExtra("email",Sender)
                .putExtra("time",timestamp)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Intent pushmsg = new Intent("pushmsg")
                .putExtra("data", msg)
                .putExtra("name",Sender);

        LocalBroadcastManager.getInstance(this).sendBroadcast(pushmsg);




        PendingIntent contentIntent = PendingIntent.getActivity(this, new Random().nextInt(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(""+Sender)
                        .setSmallIcon(R.drawable.app_icon)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}