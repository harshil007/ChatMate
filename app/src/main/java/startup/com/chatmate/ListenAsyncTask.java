package startup.com.chatmate;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Harshil on 06/04/2016.
 */
public class ListenAsyncTask extends AsyncTask<String,String,String> {
    private Socket client;
    private static InputStream inputStream;
    private static BufferedReader bufferedReader;
    private Context context;
    private String id;
    private ChatMessage main_msg;
    private String TAG = "ListenAsyncTask";
    GroupChatActivity groupChatActivity;

    public ListenAsyncTask(Context context, Socket socket, String id){
        client = socket;
        this.context = context;
        this.id =id;
        main_msg = new ChatMessage();
        groupChatActivity = new GroupChatActivity();

    }


    @Override
    protected String doInBackground(String... params) {
        String response = "";
        Log.i(TAG + id, "background task started");
        try {
            inputStream = client.getInputStream();
            byte data[] = new byte[1000];
            inputStream.read(data);
            try {
                chatmsg msg2 = chatmsg.ADAPTER.decode(data);

                main_msg.setMessageText(msg2.msgtext);
                main_msg.setUserName(msg2.username);
                main_msg.setUserType(UserType.OTHER);
                main_msg.setMessageTime(msg2.timestamp);

            } catch (IOException e) {
                e.printStackTrace();
            }
            //bufferedReader = new BufferedReader(inputStreamReader); //get the client message
/*
            while (true)
            {
                response = bufferedReader.readLine();
                if(response == null){
                    Log.i(TAG + id,"null line read");
                    break;
                }
                Log.i(TAG + id,"message read:" + response);
                JSONObject messageJson = new JSONObject(response);
                messageJson.put("id",id);
                publishProgress(new String[]{messageJson.toString()});
                Log.i(TAG + id, "message posted");
            }*/
            //Log.i(TAG + id, "connection ended");
            inputStream.close();


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }



        return response;
    }
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.i(TAG + id, "sending received intent -" + values[0]);
        groupChatActivity.receivedmsg(main_msg);

        //Intent intent = new Intent("com.excelltech.tft.server.received");
        //intent.putExtra("message",values[0]);
        //context.sendBroadcast(intent);
    }
    @Override
    protected void onPostExecute(String result){
        Log.i(TAG + id,"ENDED");

    }

    public void closeConnection(){
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}