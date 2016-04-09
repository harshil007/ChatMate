package startup.com.chatmate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by Harshil on 08/04/2016.
 */
public class chatServer {

    @SuppressWarnings("rawtypes")
    static Vector ClientSockets;
    @SuppressWarnings("rawtypes")
    static Vector LoginNames;

    public InetAddress ip;

    @SuppressWarnings({ "rawtypes", "resource" })
    chatServer() throws Exception
    {
        ip = InetAddress.getByName("127.0.0.1");
        ServerSocket soc=new ServerSocket(5217);
        ClientSockets=new Vector();
        LoginNames=new Vector();

        while(true)
        {
            Socket CSoc=soc.accept();
            new AcceptClient(CSoc);
        }
    }
    /*
    public static void main(String args[]) throws Exception
    {
        new chatServer();
    }
    */
    class AcceptClient extends Thread
    {
        Socket ClientSocket;
        DataInputStream din;
        DataOutputStream dout;
        @SuppressWarnings("unchecked")
        AcceptClient (Socket CSoc) throws Exception
        {
            ClientSocket=CSoc;

            din=new DataInputStream(ClientSocket.getInputStream());
            byte data[] = new byte[1000];
            din.read(data);
            ChatMessage dummymsg = new ChatMessage();;
            try {
                chatmsg msg2 = chatmsg.ADAPTER.decode(data);
                dummymsg.setMessageText(msg2.msgtext);
                dummymsg.setUserType(UserType.DUMMY);
                dummymsg.setUserName(msg2.username);

            } catch (IOException e) {
                e.printStackTrace();
            }


            dout=new DataOutputStream(ClientSocket.getOutputStream());
            String LoginName = dummymsg.getUserName();


            //System.out.println("User Logged In :" + LoginName);
            LoginNames.add(LoginName);
            ClientSockets.add(ClientSocket);
            start();
        }

        public void run()
        {
            while(true)
            {
                try
                {
                    String msgFromClient=new String();
                    //msgFromClient=din.readUTF();
                    byte data[] = new byte[1000];
                    din.read(data);
                    //StringTokenizer st=new StringTokenizer(msgFromClient);
                    //String Sendto=st.nextToken();
                    //String MsgType=st.nextToken();
                    ChatMessage msg = new ChatMessage();;
                    try {
                        chatmsg msg2 = chatmsg.ADAPTER.decode(data);
                        msg.setMessageText(msg2.msgtext);
                        msg.setUserType(UserType.DUMMY);
                        msg.setUserName(msg2.username);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String msgText = msg.getMessageText();

                    int iCount=0;
                    if(msgText.equals("LOGOUT"))
                    {
                        for(iCount=0;iCount<LoginNames.size();iCount++)
                        {
                            if(LoginNames.elementAt(iCount).equals(msg.getUserName()))
                            {
                                LoginNames.removeElementAt(iCount);
                                ClientSockets.removeElementAt(iCount);
                                //System.out.println("User " + Sendto +" Logged Out ...");
                                break;
                            }
                        }

                    }
                    else
                    {

                        /*while(st.hasMoreTokens())
                        {
                            msg=msg+" " +st.nextToken();
                        }*/
                        for(iCount=0;iCount<LoginNames.size();iCount++)
                        {
                            if(LoginNames.elementAt(iCount).equals(msg.getUserName()))
                            {
                                Socket tSoc=(Socket)ClientSockets.elementAt(iCount);
                                DataOutputStream tdout=new DataOutputStream(tSoc.getOutputStream());
                                //tdout.writeUTF(msg);
                                break;
                            }
                        }
                        if(iCount==LoginNames.size())
                        {
                            dout.writeUTF("I am offline");
                        }
                    }
                    //if(MsgType.equals("LOGOUT"))
                    {
                        break;
                    }

                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }
}
