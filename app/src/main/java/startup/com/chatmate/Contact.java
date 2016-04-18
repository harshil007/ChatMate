package startup.com.chatmate;

/**
 * Created by Harshil on 18/03/2016.
 */
public class Contact {

    //private variables
    int _id;
    String _name;
    String _email;

    // Empty constructor
    public Contact(){

    }
    // constructor
    public Contact(int id, String name, String _email){
        this._id = id;
        this._name = name;
        this._email = _email;
    }

    // constructor
    public Contact(String name, String _email){
        this._name = name;
        this._email = _email;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
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