package binhdang.ueh.chatify;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class MessageAdapter extends RecyclerView.Adapter{
    Context context;
    ArrayList<Message> messArraylist;
    SimpleDateFormat simpleDateFormat;
    int send = 1;
    int receive = 2;

    public MessageAdapter(Context context, ArrayList<Message> messArraylist) {
        this.context = context;
        this.messArraylist = messArraylist;
        simpleDateFormat = new SimpleDateFormat("HH:mm");

        Collections.sort(messArraylist, (c1, c2) -> c1.getTime().compareTo(c2.getTime()));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == send) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_my_bubble,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_others_bubble,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messArraylist.get(position);
        Log.d("Adding", "Ah yes!");
        if(holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder viewHolder = (SenderViewHolder)holder;
            viewHolder.textViewMessage.setText(message.getMessage());
            viewHolder.timeOfMessage.setText(simpleDateFormat.format(Long.parseLong(message.getTime())));
        }
        else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder)holder;
            viewHolder.textViewMessage.setText(message.getMessage());
            viewHolder.timeOfMessage.setText(simpleDateFormat.format(Long.parseLong(message.getTime())));
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messArraylist.get(position);
        SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        String currentUsername = sharedPref.getString("username", "");
        if(currentUsername.equals(message.getSenderName())) {
            return send;
        }
        else {
            return receive;
        }
    }

    @Override
    public int getItemCount() {
        return messArraylist.size();
    }

    static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView timeOfMessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.my_message);
            timeOfMessage = itemView.findViewById(R.id.time_of_my_message);
        }
    }

    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView timeOfMessage;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.other_message);
            timeOfMessage = itemView.findViewById(R.id.time_of_other_message);
        }
    }


}
