package startup.com.chatmate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshil on 29/03/2016.
 */
public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private final List<ChatMessage> messages;
    Context context;
    boolean group;
    


    public ChatRecyclerAdapter(Activity context, List<ChatMessage> messages, boolean group){
        this.messages = messages;
        this.context = context;
        this.group = group;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==1){
            view = inflater.inflate(R.layout.chat_me, parent, false);
        }
        else if(viewType==0){
            view = inflater.inflate(R.layout.chat_them, parent, false);
        }

        else{
            view = inflater.inflate(R.layout.chat_dummy, parent, false);
        }

        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        if (message.getUserType() == UserType.SELF) {
            return 1;
        }
        else if(message.getUserType() == UserType.OTHER){
            return 0;

        }
        else {
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        String msg = message.getMessageText();
        UserType user = message.getUserType();
        String time = message.getMessageTime();
        String userName = message.getUserName();
        if(user == UserType.SELF){
            holder.tvTextMe.setText(msg);
            holder.tvTimeMe.setText(time);
        }
        else if(user == UserType.OTHER){
            holder.tvTextThem.setText(msg);
            holder.tvTimeThem.setText(time);
            if(group){
                holder.tvUserName.setVisibility(View.VISIBLE);
                holder.tvUserName.setText(userName);
            }
        }
        else{
            holder.tvMsgDummy.setText(msg);
        }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvTextMe;
        private final TextView tvTimeMe;

        private final TextView tvTextThem;
        private final TextView tvTimeThem;

        private final TextView tvMsgDummy;
        private final TextView tvUserName;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTextMe = (TextView) itemView.findViewById(R.id.tv_chat);
            tvTimeMe = (TextView) itemView.findViewById(R.id.tv_time_me);
            tvTextThem = (TextView) itemView.findViewById(R.id.tv_chat_them);
            tvTimeThem = (TextView) itemView.findViewById(R.id.tv_time_them);
            tvMsgDummy = (TextView) itemView.findViewById(R.id.tv_chat_dummy);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_userName);

        }
    
        @Override
        public void onClick(View v) {
            /*if(clickListener!=null){
                clickListener.itemClicked(v, getPosition());
            }*/
        }

    }

    public void animateTo(List<ChatMessage> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }
    private void applyAndAnimateRemovals(List<ChatMessage> newModels) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            final ChatMessage model = messages.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    public void applyAndAnimateAdditions(List<ChatMessage> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ChatMessage model = newModels.get(i);
            if (!messages.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ChatMessage> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final ChatMessage model = newModels.get(toPosition);
            final int fromPosition = messages.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public ChatMessage removeItem(int position) {
        final ChatMessage model = messages.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, ChatMessage model) {
        messages.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ChatMessage model = messages.remove(fromPosition);
        messages.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }


}
