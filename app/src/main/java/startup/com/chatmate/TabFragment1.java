package startup.com.chatmate;


import android.content.Intent;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabFragment1 extends Fragment {

    String[] Calls = new String[] { "Harshil","Dishank","Karan","Crush" };
    ArrayList<String> calls_array = new ArrayList<String>(Arrays.asList(Calls));
    //ListView le;
    //CustomListAdapter adapter;
    RecycleAdapter adapter;
    private List<ExampleModel> mModels;

    RecyclerView lv;
    ImageView iv_search;
    TextView title;
    EditText et_search;
    int flag = 0;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_layout_2, container, false);
        //le=(ListView)view.findViewById(R.id.listView);
        lv=(RecyclerView)view.findViewById(R.id.chat_list);

        mModels = new ArrayList<>();

        for (String name : Calls) {
            mModels.add(new ExampleModel(name));
        }

        adapter=new RecycleAdapter(getActivity(),mModels);
        lv.setItemAnimator(new DefaultItemAnimator());
        lv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        lv.setAdapter(adapter);
        lv.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
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

}