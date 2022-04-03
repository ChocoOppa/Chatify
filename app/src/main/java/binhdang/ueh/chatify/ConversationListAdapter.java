package binhdang.ueh.chatify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ConversationListAdapter extends BaseAdapter {
    private Context context;

    private List<ConversationBar> testData = Arrays.asList(
            new ConversationBar(
                    "0",
                    "Takeshi",
                    "https://cdn.alongwalker.co/vn/wp-content/uploads/2022/02/16114509/image-giao-lo-noi-hang-trieu-nguoi-qua-duong-mot-ngay-o-tokyo-164496150820332.jpg",
                    "Hi!"),
            new ConversationBar(
                    "1",
                    "Hinako",
                    "https://i.pinimg.com/736x/59/18/d8/5918d8e9040516b65f93c75a9c5b8175.jpg",
                    "Thank you"),
            new ConversationBar(
                    "2",
                    "Suyuki",
                    "https://hinhnen123.com/wp-content/uploads/2021/07/Tong-hop-999-hinh-anh-Avatar-anime-cute-dep-va-an-tuong-nhat.jpg",
                    "See u later!")
    );

    public ConversationListAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return testData.size();
    }

    @Override
    public Object getItem(int i) {
        return testData.get(i);
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
        Picasso.get().load(testData.get(i).getPhotoSrc()).resize(128, 128).centerCrop().transform(new CropCircleTransformation()).into(myView.imgView);
        myView.titleTextView = view.findViewById(R.id.conversation_title);
        myView.titleTextView.setText(testData.get(i).getTitle());
        myView.lastestMsgTextView = view.findViewById(R.id.conversation_content);
        myView.lastestMsgTextView.setText(testData.get(i).getLastestMessage());
        return view;
    }

    public class MyView{
        ImageView imgView;
        TextView titleTextView;
        TextView lastestMsgTextView;
    }
}
