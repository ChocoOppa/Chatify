package binhdang.ueh.chatify;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConversationActivity extends Activity {

    TextView nameofreciever;
    EditText inputchat;
    ImageButton backbutton;
    ImageButton infobutton;
    ImageButton sendbutton;
    RecyclerView message_recyclerView;
    MessagesAdapter messAdapter;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    private  String msendername,mreceivername,senderroom, recieverroom;
    String time;
    ArrayList<Messages> messArraylist;
    Intent intent;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String enteredmessage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        nameofreciever = findViewById(R.id.nameofreciever);
        inputchat = findViewById(R.id.chat_input);
        backbutton = findViewById(R.id.back_button);
        infobutton = findViewById(R.id.info_button);
        sendbutton = findViewById(R.id.send_button);
        message_recyclerView =findViewById(R.id.message_recyclerView);
        messArraylist = new ArrayList<>();
        messAdapter=new MessagesAdapter(ConversationActivity.this,messArraylist);
        message_recyclerView.setAdapter(messAdapter);
        message_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        intent=getIntent();

        mreceivername = getIntent().getStringExtra("name");
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        SharedPreferences sharedPref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        msendername = sharedPref.getString("username", "");

        db.collection("conversationsBars")
                .whereEqualTo("forUser",msendername)
                .get()
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        Map map = doc.getData();
                        senderroom= map.get("conversation").toString();
                    }
                }
                );
        db.collection("conversationsBars")
                .whereEqualTo("otherUser",mreceivername)
                .get()
                .addOnCompleteListener(task -> {
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                Map map = doc.getData();
                                recieverroom= map.get("conversation").toString();
                            }
                        }
                );


        messAdapter = new MessagesAdapter(ConversationActivity.this,messArraylist);
        db.collection("chats")
                .get()
                .addOnCompleteListener(task -> {
                    messArraylist.clear();
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getData().size()>0) {
                        Map msg = document.getData();
                            Messages message = new
                                    Messages(msg.get("message").toString(), msg.get("time").toString()
                                    ,msg.get("senderName").toString());
                            messArraylist.add(message);
                        }
                    }
                    messAdapter=new MessagesAdapter(ConversationActivity.this,messArraylist);
                    message_recyclerView.setAdapter(messAdapter);
                    message_recyclerView.setLayoutManager(new LinearLayoutManager(this));
                });


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nameofreciever.setText(mreceivername);



        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enteredmessage=inputchat.getText().toString();
                if(enteredmessage.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter message first",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Date date = new Date();
                    time=simpleDateFormat.format(calendar.getTime());
                    Messages messages = new Messages(enteredmessage,time,msendername);
                    Map<String, Object> usersend = new HashMap<>();
                    usersend.put("conversation",senderroom);
                    usersend.put("message",enteredmessage);
                    usersend.put("time", time);
                    usersend.put("senderName", msendername);

                    Map<String, Object> userreceive = new HashMap<>();
                    userreceive.put("conversation",recieverroom);
                    userreceive.put("message",enteredmessage);
                    userreceive.put("time", time);
                    userreceive.put("senderName", msendername);


                    FirebaseFirestore.getInstance();
                    db.collection("chats")
                            .add(usersend)
                            .addOnCompleteListener(task -> {
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        db.collection("chats")
                                                .add(userreceive)
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                            }
                                        });
                                    }
                                };
                            }
                    );
                    inputchat.setText(null);
                }
            }
        });
    }
}
