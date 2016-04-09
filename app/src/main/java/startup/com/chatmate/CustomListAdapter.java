package startup.com.chatmate;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> implements Filterable{

    private final Activity context;

    private List<String> allNames;
    private List<String> filteredNames;
    private StringFilter filter;


    public CustomListAdapter(Activity context, ArrayList<String> name) {
        super(context, R.layout.samplelist, name);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.allNames = new ArrayList<String>();
        allNames.addAll(name);
        this.filteredNames = new ArrayList<String>();
        filteredNames.addAll(allNames);



        getFilter();

    }

    protected class RowViewHolder {
        public TextView text1;
        public CircularImageView imageview;
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new StringFilter();
        }
        return filter;
    }

    public View getView(int position,View view,ViewGroup parent) {
        View rowView=view;


        RowViewHolder holder;
        String m = filteredNames.get(position);
          if(rowView == null) {
              LayoutInflater inflater = context.getLayoutInflater();
              rowView = inflater.inflate(R.layout.samplelist, parent, false);

              holder = new RowViewHolder();
              holder.text1 = (TextView) rowView.findViewById(R.id.tv_name);
              holder.imageview = (CircularImageView) rowView.findViewById(R.id.listimageview);

              rowView.setTag(holder);
          }
        else {
              holder = (RowViewHolder) rowView.getTag();
          }


              holder.text1.setText(m);


        return rowView;

    };


    private class StringFilter extends Filter
    {

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

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredNames = (ArrayList<String>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = filteredNames.size(); i < l; i++)
                add(filteredNames.get(i));
            notifyDataSetInvalidated();
        }
    }




}

