package startup.com.chatmate;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Harshil on 18/04/2016.
 */
public class UserModel implements Parcelable {
    private String id;
    private String name;
    private String email;
    private String img_url;
    private int pending_msg;
    private List<ChatMessage> chatMessagesList;

    public UserModel(){

    }

    public UserModel(Parcel in) {
        name = in.readString();
        email = in.readString();
        pending_msg = in.readInt();
        in.readTypedList(chatMessagesList, ChatMessage.CREATOR);
    }

    public UserModel(String id,String name, String email,String img_url ,int pending_msg) {
        this.id=id;
        this.name = name;
        this.email = email;
        this.pending_msg = pending_msg;
        this.img_url = img_url;
    }
/*
    public UserModel(String id,String name, String email, int pending_msg) {
        this.id=id;
        this.name = name;
        this.email = email;
        this.pending_msg = pending_msg;
    }*/

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPending_msg() {
        return pending_msg;
    }

    public void setPending_msg(int pending_msg) {
        this.pending_msg = pending_msg;
    }

    public List<ChatMessage> getChatMessagesList() {
        return chatMessagesList;
    }

    public void setChatMessagesList(List<ChatMessage> chatMessagesList) {
        this.chatMessagesList = chatMessagesList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeInt(pending_msg);
        dest.writeList(chatMessagesList);
    }

    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>()
    {
        public UserModel createFromParcel(Parcel in)
        {
            return new UserModel(in);
        }
        public UserModel[] newArray(int size)
        {
            return new UserModel[size];
        }
    };
}
