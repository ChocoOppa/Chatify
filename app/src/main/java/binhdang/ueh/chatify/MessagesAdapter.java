package binhdang.ueh.chatify;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter{
    Context context;
    ArrayList<Messages> messArraylist;
    int send = 1;
    int receive = 2;

    public MessagesAdapter(Context context, ArrayList<Messages> messArraylist) {
        this.context = context;
        this.messArraylist = messArraylist;
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
        Messages messages = messArraylist.get(position);
        if(holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder viewHolder = (SenderViewHolder)holder;
            viewHolder.textViewMessage.setText(messages.getMessage());
            viewHolder.timeOfMessage.setText(messages.getTime());
        }
        else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder)holder;
            viewHolder.textViewMessage.setText(messages.getMessage());
            viewHolder.timeOfMessage.setText(messages.getTime());
        }

    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messArraylist.get(position);
        SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        String currentUsername = sharedPref.getString("username", "");
        if(currentUsername.equals(messages.getSenderName())) {
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
