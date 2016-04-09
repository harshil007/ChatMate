package startup.com.chatmate;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TabFragment3 extends Fragment implements RecycleAdapter.ClickListener,RecycleAdapter.onLongListener{
    String[] Contacts = new String[] { "Harshil","Dishank","Karan","Param","Kunal","Keyur","Maikhar","Mohit","Dipshil","Crush","Crusher","Emma Watson","Jim Carrey","Cristiano Ronaldo","Batman"};
    ArrayList<String> contacts_array = new ArrayList<String>(Arrays.asList(Contacts));
    private List<ExampleModel> mModels;

    //CustomListAdapter adapter;
    RecycleAdapter adapter;

    RecyclerView lv;
    String items[]={"Delete","Update","Cancel"};


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_layout_3, container, false);
        db = new DBhandler(getActivity());
        final View promptsView = inflater.inflate(R.layout.custom_prompt, null);



        et_name = (EditText)promptsView.findViewById(R.id.et_name);
        et_no = (EditText)promptsView.findViewById(R.id.et_no);






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
        List<Contact> contacts = db.getAllContacts();
        for (Contact cn : contacts) {
            mModels.add(new ExampleModel(cn.getName()));
        }


        adapter=new RecycleAdapter(getActivity(),mModels);

        lv.setItemAnimator(new DefaultItemAnimator());
        lv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lv.setAdapter(adapter);
        adapter.setClickListener(this);
        adapter.setonLongListener(this);
        lv.setLayoutManager(new LinearLayoutManager(getActivity()));





/*
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle("Contact details")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                String name = et_name.getText().toString();
                                String no = et_no.getText().toString();

                                //if(!(name.equals("") && no.equals(""))){
                                db.addContact(new Contact(name,no));
                                mModels.add(new ExampleModel(name));
                                adapter.applyAndAnimateAdditions(mModels);
                                Toast.makeText(getActivity(), "Contact added", Toast.LENGTH_SHORT);
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

        final AlertDialog alertDialog = alertDialogBuilder.create();*/




        b_add = (Button)view.findViewById(R.id.b_add);
        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1);

            }
        });




        return view;
    }


    public void deleteAll(){
        db.deleteAll();
        List<Contact> contacts = db.getAllContacts();
        for (Contact cn : contacts) {
            mModels.add(new ExampleModel(cn.getName()));
        }
        adapter.applyAndAnimateAdditions(mModels);
        Toast.makeText(getActivity(),"All contacts deleted",Toast.LENGTH_SHORT).show();


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();

                Cursor c = getActivity().getContentResolver().query(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String hasPhone =
                            c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);

                        String name=c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        phones.moveToFirst();
                        String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        Toast.makeText(getActivity(), cNumber, Toast.LENGTH_SHORT).show();
                        //setCn(cNumber);

                        Contact contact = new Contact();
                        contact.setName(name);
                        contact.setPhoneNumber(cNumber);
                        db.addContact(contact);
                        mModels.add(new ExampleModel(name));
                        adapter.applyAndAnimateAdditions(mModels);
                        Toast.makeText(getActivity(), "Contact added", Toast.LENGTH_SHORT).show();


                    }
                }

            }
        }
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
                final List<ExampleModel> filteredModelList = filter(mModels, s.toString());
                adapter.animateTo(filteredModelList);
                lv.scrollToPosition(0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private List<ExampleModel> filter(List<ExampleModel> models, String query) {
        query = query.toLowerCase();

        final List<ExampleModel> filteredModelList = new ArrayList<>();
        for (ExampleModel model : models) {
            final String text = model.getText().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    @Override
    public void itemClicked(View view, int position) {
        TextView tv = (TextView) view.findViewById(R.id.tv_name);
        String name = tv.getText().toString();
        Intent i = new Intent(getActivity(), ChatActivity.class);
        i.putExtra("name", name);
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

                } else if (items[item].equals("Update")) {

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}