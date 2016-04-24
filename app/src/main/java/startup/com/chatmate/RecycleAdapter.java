package startup.com.chatmate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Harshil on 07/03/2016.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<String> allNames;
    private List<String> filteredNames;
    //private StringFilter filter;
    private ArrayList<String> name_array;
    Context context;
    private final List<UserModel> mModels;
    ClickListener clickListener;
    onLongListener onLongClick;
    int tab;


    public RecycleAdapter(FragmentActivity context, List<UserModel> models,int tab) {
        inflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        this.tab=tab;
        /*name_array = name;
        this.allNames = new ArrayList<String>();
        allNames.addAll(name);
        this.filteredNames = new ArrayList<String>();
        filteredNames.addAll(allNames);
        this.context = context;
        //getFilter();*/
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.samplelist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final UserModel model = mModels.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void animateTo(List<UserModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }
    private void applyAndAnimateRemovals(List<UserModel> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final UserModel model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    public void applyAndAnimateAdditions(List<UserModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final UserModel model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<UserModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final UserModel model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public UserModel removeItem(int position) {
        final UserModel model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, UserModel model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final UserModel model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }


    public void setClickListener(ClickListener clickListener){
        this.clickListener=clickListener;
    }

    public void setonLongListener(onLongListener onLongClick){this.onLongClick=onLongClick;}

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        private final TextView tvText,tvEmail;
        private final ImageView iv_profile;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            tvText = (TextView) itemView.findViewById(R.id.tv_name);
            tvEmail = (TextView) itemView.findViewById(R.id.tv_last_chat);
            iv_profile = (ImageView) itemView.findViewById(R.id.iv_chat_profile);
        }

        public void bind(UserModel model) {
            tvText.setText(model.getName());
            if(tab==3){
                tvEmail.setText(model.getEmail());
            }
            Picasso.with(context)
                    .load(model.getImg_url())
                    .placeholder(R.drawable.ic_account)
                    .into(iv_profile);
        }

        @Override
        public void onClick(View v) {
            if(clickListener!=null){
                clickListener.itemClicked(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(onLongClick!=null){
                onLongClick.itemLongClicked(v,getPosition());
            }

            return false;
        }
    }

    public interface ClickListener{

        void itemClicked(View view, int position);
    }

    public interface onLongListener{
        void itemLongClicked(View view,int position);
    }

/*
    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name;
        CircularImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            image = (CircularImageView) itemView.findViewById(R.id.listimageview);
        }
    }





    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new StringFilter(this, allNames);
        }
        return filter;
    }

    private class StringFilter extends Filter {

        private final RecycleAdapter adapter;

        private final List<String> originalList;

        private ArrayList<String> filteredItems = new ArrayList<String>();

        private StringFilter(RecycleAdapter adapter, List<String> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<String> filteredItems = new ArrayList<String>();

                for(int i = 0, l = allNames.size(); i < l; i++)
                {
                    String m = allNames.get(i);
                    if(m.toLowerCase().contains(constraint))
                        filteredItems.add(m);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = allNames;
                    result.count = allNames.size();
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredNames.clear();
            adapter.filteredNames.addAll((ArrayList<String>) results.values);
            adapter.notifyDataSetChanged();
        }
    }*/


}