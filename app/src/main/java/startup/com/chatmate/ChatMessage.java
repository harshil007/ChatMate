package startup.com.chatmate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Harshil on 29/03/2016.
 */
public class ChatMessage implements Parcelable {

    private String messageText;
    private UserType userType;
    private Status messageStatus;
    private String userName;

    public ChatMessage(){

    }

    public ChatMessage(Parcel in) {
        messageText = in.readString();
        String type = in.readString();
        String dummy = UserType.DUMMY.toString();
        String self = UserType.SELF.toString();
        String other = UserType.OTHER.toString();
        switch (type){
            case "SELF":
                userType = UserType.SELF;
                break;
            case "OTHER":
                userType = UserType.OTHER;
                break;
            case "DUMMY":
                userType = UserType.DUMMY;
                break;

        }
        String status = in.readString();
        switch (status){
            case "SENT":
                messageStatus = Status.SENT;
                break;
            case "DELIVERED":
                messageStatus = Status.DELIVERED;
                break;


        }

        userName = in.readString();
    }

    public String getMessageTime() {
        return messageTime;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName=userName;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    private String messageTime;

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setMessageStatus(Status messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessageText() {

        return messageText;
    }

    public UserType getUserType() {
        return userType;
    }

    public Status getMessageStatus() {
        return messageStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(messageText);
        dest.writeString(userType.toString());
        dest.writeString(messageStatus.toString());
        dest.writeString(userName);
    }

    public static final Parcelable.Creator<ChatMessage> CREATOR = new Parcelable.Creator<ChatMessage>()
    {
        public ChatMessage createFromParcel(Parcel in)
        {
            return new ChatMessage(in);
        }
        public ChatMessage[] newArray(int size)
        {
            return new ChatMessage[size];
        }
    };
}
