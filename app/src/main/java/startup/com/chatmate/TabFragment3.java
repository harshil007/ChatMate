package startup.com.chatmate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TabFragment3 extends Fragment implements RecycleAdapter.ClickListener,RecycleAdapter.onLongListener{
    String[] Contacts = new String[] { "Harshil","Dishank","Karan","Param","Kunal","Keyur","Maikhar","Mohit","Dipshil","Crush","Crusher","Emma Watson","Jim Carrey","Cristiano Ronaldo","Batman"};
    ArrayList<String> contacts_array = new ArrayList<String>(Arrays.asList(Contacts));
    private List<UserModel> mModels;


    RecycleAdapter adapter;

    RecyclerView lv;
    String items[]={"Delete","Update","Cancel"};
    String URL = "http://chatmate.comlu.com/fetch_user.php";
    ProgressDialog pDialog;


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    ImageView iv_search;
    TextView title,tv_time,tv_chat;
    EditText et_search,et_name,et_no;
    int flag = 0;
    Toolbar toolbar;
    Button b_add;
    DBhandler db;
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_layout_3, container, false);
        db = new DBhandler(getActivity());
        final View promptsView = inflater.inflate(R.layout.custom_prompt, null);
        mQueue = CustomVolleyRequestQueue.getInstance(getActivity().getApplicationContext())
                .getRequestQueue();

        et_no = (EditText)promptsView.findViewById(R.id.et_no);

        pDialog = new ProgressDialog(getActivity());




        //le=(ListView)view.findViewById(R.id.listView);
        lv=(RecyclerView)view.findViewById(R.id.contact_list);
        //tv_time=(TextView) view.findViewById(R.id.tv_time);
        //tv_chat=(TextView)view.findViewById(R.id.tv_last_chat);
        //tv_chat.setVisibility(View.INVISIBLE);
        //tv_time.setVisibility(View.INVISIBLE);





        mModels = new ArrayList<>();
       /* for (String name : Contacts) {
            mModels.add(new ExampleModel(name));
        }*/

        //db.deleteAll();


        adapter=new RecycleAdapter(getActivity(),mModels,3);

        lv.setItemAnimator(new DefaultItemAnimator());
        lv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lv.setAdapter(adapter);
        adapter.setClickListener(this);
        adapter.setonLongListener(this);
        lv.setLayoutManager(new LinearLayoutManager(getActivity()));


        List<Contact> contacts = db.getAllContacts();
        for (Contact cn : contacts) {
            UserModel user = new UserModel(cn.getID(),cn.getName(),cn.getEmail(),cn.getImg_url(),0);
            mModels.add(user);
            adapter.animateTo(mModels);
        }




        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle("Google plus Email id:")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                String no = et_no.getText().toString().trim();
                                pDialog.setMessage("Connecting to server");
                                pDialog.setCancelable(false);
                                pDialog.show();
                                volleyQuery(no);

                                //if(!(name.equals("") && no.equals(""))){
                                                               dialog.cancel();
                                // }else{
                                //    Toast.makeText(getActivity(),"Invalid input",Toast.LENGTH_SHORT);
                                //}


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilder.create();




        b_add = (Button)view.findViewById(R.id.b_add);
        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            alertDialog.show();

            }
        });




        return view;
    }

    private void volleyQuery(final String email) {
        Log.i("email",email);
        JSONObject son = new JSONObject();
        try {
            son.put("email", email);


        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonArrayRequest jreq = new JsonArrayRequest(Request.Method.POST,URL,son, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                pDialog.dismiss();
                try {
                    JSONObject result = jsonArray.getJSONObject(0);
                    JSONArray name = jsonArray.getJSONArray(1);
                    JSONArray id_array = jsonArray.getJSONArray(2);
                    JSONArray img_array = jsonArray.getJSONArray(3);
                    int success = result.getInt("success");
                    Log.i("success",""+success);
                    if(success==1){
                        db.addContact(new Contact(id_array.getString(0),name.getString(0),email,img_array.getString(0)));
                        mModels.add(new UserModel(id_array.getString(0),name.getString(0),email,img_array.getString(0),0));
                        adapter.applyAndAnimateAdditions(mModels);
                        Toast.makeText(getActivity(), "Contact added", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                volleyError.printStackTrace();
            }
        });

        jreq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(jreq);

    }


    public void deleteAll(){
        db.deleteAll();
        List<Contact> contacts = db.getAllContacts();
        for (Contact cn : contacts) {
            UserModel user = new UserModel(cn.getID(),cn.getName(),cn.getEmail(),cn.getImg_url(),0);
            mModels.add(user);
        }
        adapter.applyAndAnimateAdditions(mModels);
        Toast.makeText(getActivity(),"All contacts deleted",Toast.LENGTH_SHORT).show();


    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        et_search = (EditText) getActivity().findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final List<UserModel> filteredModelList = filter(mModels, s.toString());
                adapter.animateTo(filteredModelList);
                lv.scrollToPosition(0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private List<UserModel> filter(List<UserModel> models, String query) {
        query = query.toLowerCase();

        final List<UserModel> filteredModelList = new ArrayList<>();
        for (UserModel model : models) {
            final String text = model.getName().toLowerCase();
            final String email = model.getEmail();
            if (text.contains(query) || email.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    @Override
    public void itemClicked(View view, int position) {
        TextView tv = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_email = (TextView) view.findViewById(R.id.tv_last_chat);
        String name = tv.getText().toString();
        String email = tv_email.getText().toString();
        UserModel usr = mModels.get(position);
        String id = usr.getId();
        String img_url = usr.getImg_url();
        Intent i = new Intent(getActivity(), ChatActivity.class);
        i.putExtra("id",id);
        i.putExtra("name", name);
        i.putExtra("email",email);
        i.putExtra("img_url",img_url);
        UserModel user = new UserModel(id,name,email,img_url,0);
        ((MainActivity)getActivity()).update_chat_list(user);
        startActivity(i);
    }




    @Override
    public void itemLongClicked(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Delete")) {
                    //db.deleteContact();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}