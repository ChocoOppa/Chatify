package binhdang.ueh.chatify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ConversationListAdapter extends BaseAdapter {
    private Context context;
    private List<ConversationBar> data = new ArrayList<ConversationBar>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    MyView currentView;

    public ConversationListAdapter(Context context, List<ConversationBar> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyView myView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null) {
            myView = new MyView();
            view = inflater.inflate(R.layout.conversation_bar, null);
            myView.imgView = view.findViewById(R.id.conversation_img);
            view.setTag(myView);
        }
        else {
            myView = (MyView)view.getTag();
        }
        currentView = myView;
        Log.d("Data size", String.valueOf(data.size()));
        Collections.sort(data, new Comparator<ConversationBar>() {
            @Override
            public int compare(ConversationBar c1, ConversationBar c2) {
                return c2.getLastMessageTime().compareTo(c1.getLastMessageTime());
            }
        });
        if(data.size() > 0) {
            StorageReference storageRef = storage.getReference();
            StorageReference pfp = storageRef.child(data.get(i).getPhotoSrc());
            pfp.getDownloadUrl().addOnSuccessListener(uri ->
                    Picasso.get().load(uri.toString()).
                            resize(128, 128).centerCrop().
                            transform(new CropCircleTransformation()).into(myView.imgView));
            myView.titleTextView = view.findViewById(R.id.conversation_title);
            myView.titleTextView.setText(data.get(i).getTitle());
            myView.lastMsgTextView = view.findViewById(R.id.conversation_content);
            myView.lastMsgTextView.setText(data.get(i).getLastMessage());
        }
        else{
            myView.titleTextView = view.findViewById(R.id.conversation_title);
            myView.titleTextView.setText("");
            myView.lastMsgTextView = view.findViewById(R.id.conversation_content);
            myView.lastMsgTextView.setText("");
            view.setEnabled(false);
        }
        return view;
    }

    public class MyView{
        ImageView imgView;
        TextView titleTextView;
        TextView lastMsgTextView;
    }
}
