package binhdang.ueh.chatify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ConversationActivity extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView conversationTitle;
    EditText inputChat;
    ImageButton backButton;
    ImageButton infoButton;
    ImageButton sendButton;
    RecyclerView messageRecyclerView;

    MessagesAdapter messageAdapter;
    ArrayList<Messages> messageArrayList;
    String currentUsername, conversation;
    String time;
    SimpleDateFormat simpleDateFormat;
    String enteredMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        messageArrayList = new ArrayList<>();

        conversationTitle = findViewById(R.id.conversation_title_on_bar);
        inputChat = findViewById(R.id.chat_input);
        backButton = findViewById(R.id.back_button);
        infoButton = findViewById(R.id.info_button);
        sendButton = findViewById(R.id.send_button);
        messageRecyclerView =findViewById(R.id.message_recyclerView);

        ChatDataUpdate();

        conversationTitle.setText(getIntent().getStringExtra("name"));
        conversation = getIntent().getStringExtra("conversation");
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        SharedPreferences sharedPref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        currentUsername = sharedPref.getString("username", "");

        db.collection("chats")
                .whereEqualTo("conversation", conversation)
                .get()
                .addOnCompleteListener(task -> {
                    messageArrayList.clear();
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getData().size() > 0) {
                        Map msg = document.getData();
                            Messages message = new Messages(msg.get("message").toString(),
                                    simpleDateFormat.format(Long.parseLong(msg.get("time").toString())),
                                    msg.get("senderName").toString());
                            messageArrayList.add(message);
                        }
                    }
                    ChatDataUpdate();
                });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sendButton.setOnClickListener(view -> {
            enteredMessage = inputChat.getText().toString();
            if(enteredMessage.isEmpty()) {
                Toast.makeText(getApplicationContext(),"Enter message first",Toast.LENGTH_SHORT).show();
            }
            else {
                time = String.valueOf(System.currentTimeMillis());
                Map<String, Object> message = new HashMap<>();
                message.put("conversation" , conversation);
                message.put("message", enteredMessage);
                message.put("time", time);
                message.put("senderName", currentUsername);

                db.collection("chats")
                        .add(message)
                        .addOnCompleteListener(task -> {
                            ChatDataUpdate();
                        }
                );
                inputChat.setText("");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ChatDataUpdate();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messageAdapter!=null) {
            ChatDataUpdate();
        }
    }

    private void ChatDataUpdate(){
        messageAdapter = new MessagesAdapter(ConversationActivity.this, messageArrayList);
        messageRecyclerView.setAdapter(messageAdapter);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
