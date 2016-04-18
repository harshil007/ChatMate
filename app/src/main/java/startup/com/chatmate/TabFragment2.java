package startup.com.chatmate;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabFragment2 extends Fragment implements RecycleAdapter.ClickListener{

    String[] Chat = new String[] { "Harshil","Dishank","Karan","Param","Kunal","Keyur","Maikhar","Mohit","Dipshil","Crush","Crusher","Cristiano Ronaldo"};
    ArrayList<String> chat_array = new ArrayList<String>(Arrays.asList(Chat));
    private List<UserModel> mModels;



    RecycleAdapter adapter;
    LinearLayout ll;
    RecyclerView lv;
    ImageView iv_search;
    TextView title;
    EditText et_search;
    int flag = 0;
    Toolbar toolbar;
    View vw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_layout_2, container, false);
        //le=(ListView)view.findViewById(R.id.listView);
        lv=(RecyclerView)view.findViewById(R.id.chat_list);
        lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = lv.getChildAdapterPosition(v);
                String name = Chat[position];
                Intent i = new Intent(getActivity(), ChatActivity.class);
                i.putExtra("name", name);
                startActivity(i);
            }
        });

        mModels = new ArrayList<>();

        /*
        for (String name : Chat) {
            mModels.add(new UserModel(name,"",));
        }
        */



        adapter=new RecycleAdapter(getActivity(),mModels,2);
        lv.setItemAnimator(new DefaultItemAnimator());
        lv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lv.setAdapter(adapter);

        lv.setLayoutManager(new LinearLayoutManager(getActivity()));
        lv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int finalRadius = (int)Math.hypot(v.getWidth()/2,v.getHeight()/2);
                //Animator anim = ViewAnimationUtils.createCircularReveal(v,(int) v.getWidth()/2, (int) v.getHeight()/2,0, finalRadius);
                v.setBackgroundColor(Color.BLUE);
                //anim.start();

                return false;
            }
        });

        adapter.setClickListener(this);

        return view;
    }

    public void update_list(UserModel userModel){
        mModels.add(userModel);
        adapter.animateTo(mModels);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        title = (TextView) getActivity().findViewById(R.id.tv_title);
        et_search = (EditText) getActivity().findViewById(R.id.et_search);
        iv_search = (ImageView) getActivity().findViewById(R.id.search_view);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    title.setVisibility(View.INVISIBLE);
                    et_search.setVisibility(View.VISIBLE);
                    et_search.requestFocus();
                    toolbar.setBackgroundColor(Color.WHITE);
                    imm.showSoftInput(et_search, InputMethodManager.SHOW_IMPLICIT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        iv_search.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel_black_24dp, getActivity().getTheme()));
                    } else {
                        iv_search.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel_black_24dp));
                    }
                    flag = 1;
                } else {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    title.setVisibility(View.VISIBLE);
                    et_search.setVisibility(View.INVISIBLE);
                    et_search.setText("");
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        iv_search.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_black_24dp, getActivity().getTheme()));
                    } else {
                        iv_search.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_black_24dp));
                    }
                    flag = 0;
                }

            }
        });

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
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void itemClicked(View view, int position) {
        TextView tv = (TextView)view.findViewById(R.id.tv_name);
        String name =tv.getText().toString();
        Intent i = new Intent(getActivity(), ChatActivity.class);
        i.putExtra("name", name);
        startActivity(i);
    }
}