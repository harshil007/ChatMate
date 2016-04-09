package startup.com.chatmate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Harshil on 05/04/2016.
 */
public class OfflineFragment extends Fragment implements View.OnClickListener{

    FloatingActionButton fab_create,fab_join;
    public AlertDialog alertDialog;
    EditText et_grp_name;
    View custView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offline_main, container, false);
        fab_create = (FloatingActionButton) view.findViewById(R.id.fab_create);
        fab_join = (FloatingActionButton) view.findViewById(R.id.fab_join);

        custView = inflater.inflate(R.layout.alert_grp_name, null);
        et_grp_name =(EditText) custView.findViewById(R.id.et_grp_name);


        fab_create.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(custView);
        alertDialogBuilder.setTitle("Create Group")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                String name = et_grp_name.getText().toString();


                                if (!name.equals("")) {

                                    Intent i = new Intent(getActivity(),GroupChatActivity.class);
                                    i.putExtra("type","SERVER");
                                    i.putExtra("name",name);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT).show();
                                }


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        alertDialog = alertDialogBuilder.create();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_create:



                break;

            case R.id.fab_join:


                break;

        }
    }
}
