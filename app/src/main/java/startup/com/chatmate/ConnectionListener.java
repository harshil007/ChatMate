package startup.com.chatmate;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Harshil on 06/04/2016.
 */
public class ConnectionListener extends AsyncTask<String,Socket,String> {
    private static ServerSocket serverSocket;



    private Context context;
    //private GameState gameState;
    int idCounter;
    private String TAG = "Connection Listener";
    private ArrayList<SocketConnectionManager> socketConnections;

    public ConnectionListener(Context context, ServerSocket serverSocket){
        super();

        this.context = context;
        this.serverSocket = serverSocket;
        idCounter = 0;
        socketConnections = new ArrayList<SocketConnectionManager>();
    }

    @Override
    protected String doInBackground(String... params) {
        String response = "";

        Log.i(TAG, "Start listening for connections");
        while (true) {
            try {
                Log.i(TAG, "Still Looking");
                Socket clientSocket = serverSocket.accept();   //accept the client connection
                Log.i(TAG, "Connection Found");
                publishProgress(new Socket[] {clientSocket});
            } catch (IOException ex) {
                System.out.println("Problem in message reading");
            } catch (Exception e){
            }
        }
    }

    @Override
    protected void onProgressUpdate(Socket... values) {
        super.onProgressUpdate(values);
        Log.i(TAG, "start socket connection");
        idCounter++;
        SocketConnectionManager socketConectionManager = new SocketConnectionManager(context,values[0],String.valueOf(idCounter));
        Log.i(TAG, "about to execute");
        //socketConectionManager.executeOnExecutor(((FireTvApplication) context).threadPoolExecutor, (String[])null);
        Log.i(TAG, "executed");
        socketConnections.add(socketConectionManager);
    }
    @Override
    protected void onPostExecute(String result){
    }
}