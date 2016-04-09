package startup.com.chatmate;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Harshil on 06/04/2016.
 */
public class SocketConnectionManager extends AsyncTask<String,String,String> {

    private Context context;
    //private MessageAsyncTask messageAsyncTask;
    private ListenAsyncTask listenAsyncTask;
    private String ipAddress;
    private Socket socket;
    private String id;

    public SocketConnectionManager(Context context, Socket socket, String id){
        this.context = context;
        this.socket = socket;
        this.id = id;

    }

    @Override
    protected String doInBackground(String... params) {
        Log.i("SocketConnetionManger", "start background task");

        startConnection();
        return "";
    }
    public void startConnection(){
        Log.i("SocketConnetionManger", "start connection");

        try{
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader  bufferedReader = new BufferedReader(inputStreamReader); //get the client message
            Log.i("SocketConnetionManger", "waiting to read line");
            String response = bufferedReader.readLine();
            if(response == null){
                Log.i("SocketConnetionManger","null line read - just ip test from client");
            }
            else{
                Log.i("SocketConnetionManger", "message read:" + response);
                //messageAsyncTask = new MessageAsyncTask(context,socket,id);
                listenAsyncTask = new ListenAsyncTask(context,socket,id);
                //messageAsyncTask.executeOnExecutor(((FireTvApplication) context).threadPoolExecutor, (String[])null);
                //listenAsyncTask.executeOnExecutor(((FireTvApplication) context).threadPoolExecutor, (String[])null);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
