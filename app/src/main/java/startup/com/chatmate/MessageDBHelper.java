package startup.com.chatmate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshil on 07/04/2016.
 */
public class MessageDBHelper extends SQLiteOpenHelper{


    private static final String DB_NAME = "main";
    private static String TABLE_MESSAGE;
    private static final String KEY_ID = "_id";
    private static final String MSG = "message";
    private static final String USER_TYPE = "user_type";
    private static final String TIME = "time";
    private static final int DATABASE_VERSION=1;

    public static int TABLE_CREATED = 0;


    public MessageDBHelper(Context context, String table_name) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        TABLE_MESSAGE = table_name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MESSAGE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + MSG + " TEXT,"
                + TIME + " TEXT," + USER_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        TABLE_CREATED=1;
        //create_table(db);
    }

    public void create_table(String tb_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tb_name);
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + tb_name + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + MSG + " TEXT,"
                + TIME + " TEXT," + USER_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        TABLE_CREATED=1;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);

        onCreate(db);
    }

    public void addMessage(ChatMessage message){
        String msg = message.getMessageText();
        String time = message.getMessageTime();
        String user_type = message.getUserType().toString();

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MSG,msg);
        values.put(TIME,time);
        values.put(USER_TYPE,user_type);

        db.insert(TABLE_MESSAGE, null, values);
        db.close();

    }

    public void removeAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_MESSAGE);
        db.close();
    }

    public int delete(ChatMessage message){
        String msg = message.getMessageText();
        String time = message.getMessageTime();
        String user_type = message.getUserType().toString();



        SQLiteDatabase db = this.getWritableDatabase();
        int c = db.delete(TABLE_MESSAGE,MSG+"=? AND " +TIME+"=? AND "+USER_TYPE+"=?",new String[]{msg,time,user_type});
        return c;
    }

    public List<ChatMessage> fetch_messages(){
        List<ChatMessage> chat_array = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ChatMessage msg = new ChatMessage();
                msg.setMessageText(cursor.getString(1));
                msg.setMessageTime(cursor.getString(2));
                String user = cursor.getString(3);
                UserType userType = UserType.SELF;
                switch (user){
                    case "SELF" :
                        userType = UserType.SELF;
                        break;
                    case "OTHER" :
                        userType = UserType.OTHER;
                        break;
                    case "DUMMY" :
                        userType = UserType.DUMMY;
                        break;

                }
                msg.setUserType(userType);
                // Adding contact to list
                chat_array.add(msg);
            } while (cursor.moveToNext());
        }

        // return contact list
        return chat_array;

    }

}