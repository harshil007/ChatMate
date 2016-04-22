package startup.com.chatmate;

/**
 * Created by Harshil on 18/03/2016.
 */
public class Contact {

    //private variables
    String _id;
    String _name;
    String _email;
    String img_url;



    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    // Empty constructor
    public Contact(){

    }
    // constructor
    public Contact(String id, String name, String _email,String img_url){
        this._id = id;
        this._name = name;
        this._email = _email;
        this.img_url = img_url;
    }



    // constructor
    public Contact(String name, String _email){
        this._name = name;
        this._email = _email;
    }
    // getting ID
    public String getID(){
        return this._id;
    }

    // setting id
    public void setID(String id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting phone number
    public String getEmail(){
        return this._email;
    }

    // setting phone number
    public void setEmail(String email){
        this._email = email;
    }
}