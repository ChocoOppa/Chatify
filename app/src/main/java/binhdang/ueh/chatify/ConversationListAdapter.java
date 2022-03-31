package binhdang.ueh.chatify;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

//import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ConversationListAdapter extends BaseAdapter {
    private Context context;

    private List<ConversationBar> testData = Arrays.asList(
            new ConversationBar(
                    "1",
                    "Adam",
                    "https://images.pexels.com/photos/358238/pexels-photo-358238.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "sup bro!"),
            new ConversationBar(
                    "2",
                    "Beth",
                    "https://thumbs.dreamstime.com/b/beautiful-golden-autumn-scenery-trees-golden-leaves-sunshine-scotland-united-kingdom-beautiful-golden-autumn-124278811.jpg",
                    "where r u?"),
            new ConversationBar(
                    "3",
                    "Charlie",
                    "https://media.istockphoto.com/photos/picturesque-morning-in-plitvice-national-park-colorful-spring-scene-picture-id1093110112?k=20&m=1093110112&s=612x612&w=0&h=3OhKOpvzOSJgwThQmGhshfOnZTvMExZX2R91jNNStBY=",
                    "Hav fun!"),
            new ConversationBar(
                    "4",
                    "Dave",
                    "https://media.istockphoto.com/photos/picturesque-morning-in-plitvice-national-park-colorful-spring-scene-picture-id1093110112?k=20&m=1093110112&s=612x612&w=0&h=3OhKOpvzOSJgwThQmGhshfOnZTvMExZX2R91jNNStBY=",
                    "yo"),
            new ConversationBar(
                    "5",
                    "Emily",
                    "https://www.traveloffpath.com/wp-content/uploads/2021/11/japan-scenery.jpg",
                    "Hi there!"),
            new ConversationBar(
                    "6",
                    "Francis",
                    "https://media.istockphoto.com/photos/fuji-mountain-red-maple-tree-and-fisherman-boat-with-morning-mist-in-picture-id1192780580?k=20&m=1192780580&s=612x612&w=0&h=STSjwbr2JAtCkkBb2yGGWl1IYhxb_ro5XBS-JRnpgmw=",
                    "come here"),
            new ConversationBar(
                    "7",
                    "Gustav",
                    "https://medical-treatment-japan.jp/en/wp-content/uploads/sites/2/2018/05/fuji-1.png",
                    "okay"),
            new ConversationBar(
                    "8",
                    "Takeshi",
                    "https://cdn.alongwalker.co/vn/wp-content/uploads/2022/02/16114509/image-giao-lo-noi-hang-trieu-nguoi-qua-duong-mot-ngay-o-tokyo-164496150820332.jpg",
                    "Hi!"),
            new ConversationBar(
                    "9",
                    "Hinako",
                    "https://i.pinimg.com/736x/59/18/d8/5918d8e9040516b65f93c75a9c5b8175.jpg",
                    "Thank you"),
            new ConversationBar(
                    "10",
                    "Suyuki",
                    "https://hinhnen123.com/wp-content/uploads/2021/07/Tong-hop-999-hinh-anh-Avatar-anime-cute-dep-va-an-tuong-nhat.jpg",
                    "See u later!"),
            new ConversationBar(
                    "11",
                    "Violet",
                    "https://i.pinimg.com/564x/d9/8e/8f/d98e8fb8d09bae68a580b74625f6ce74.jpg",
                    "What will be the approriate time? Maybe tommorow morning?")
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
        //Picasso.get().load(testData.get(i).getPhotoSrc()).resize(128, 128).centerCrop().transform(new CropCircleTransformation()).into(myView.imgView);
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
