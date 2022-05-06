package binhdang.ueh.chatify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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

    MessageAdapter messageAdapter;
    ArrayList<Message> messageArrayList;
    String currentUsername, conversation;
    String type;
    String time;

    String enteredMessage;

    private static ConversationActivity currentConversation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        messageArrayList = new ArrayList<>();

        currentConversation = this;

        conversationTitle = findViewById(R.id.conversation_title_on_bar);
        inputChat = findViewById(R.id.chat_input);
        backButton = findViewById(R.id.back_button);

        infoButton = findViewById(R.id.info_button);

        sendButton = findViewById(R.id.send_button);
        messageRecyclerView =findViewById(R.id.message_recyclerView);

        retrieveChatData();

        conversationTitle.setText(getIntent().getStringExtra("name"));
        conversation = getIntent().getStringExtra("conversation");

        db.collection("conversations")
                .whereEqualTo("conversation", conversation)
                .get()
                .addOnCompleteListener(task -> {
                   for(QueryDocumentSnapshot document: task.getResult()){
                       Map conversation = document.getData();
                       type = conversation.get("type").toString();
                   }
                });

        db.collection("chats")
                .whereEqualTo("conversation", conversation)
                .addSnapshotListener(eventListener);

        SharedPreferences sharedPref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        currentUsername = sharedPref.getString("username", "");

        backButton.setOnClickListener(view -> finish());

        infoButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ViewConversationInfoActivity.class);
            intent.putExtra("conversation", conversation);
            intent.putExtra("type", type);
            startActivity(intent);
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
                            retrieveChatData();
                        }
                );

                inputChat.setText("");

                db.collection("conversationsBars")
                        .whereEqualTo("conversation", conversation)
                        .get()
                        .addOnCompleteListener(task -> {
                            for (QueryDocumentSnapshot document: task.getResult()){
                                String docId = document.getId();

                                db.collection("conversationsBars")
                                        .document(docId)
                                        .update("lastConversationMsg", enteredMessage);

                                db.collection("conversationsBars")
                                        .document(docId)
                                        .update("lastConversationTime", time);
                            }
                        });
            }
        });
    }

    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        if (error != null){
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    retrieveChatData();
                }
            }
        }
    });

    private void ChatDataUpdate(){
        messageAdapter = new MessageAdapter(ConversationActivity.this, messageArrayList);
        messageRecyclerView.setAdapter(messageAdapter);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.scrollToPosition(messageArrayList.size() - 1);
    }

    private void retrieveChatData(){
        db.collection("chats")
                .whereEqualTo("conversation", conversation)
                .get()
                .addOnCompleteListener(task -> {
                    messageArrayList.clear();
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getData().size() > 0) {
                            Map msg = document.getData();
                            Message message = new Message(msg.get("message").toString(),
                                    msg.get("time").toString(),
                                    msg.get("senderName").toString());
                            messageArrayList.add(message);
                        }
                    }
                    if (messageArrayList.size() > 0){
                        ChatDataUpdate();
                    }
                });
    }

    public static ConversationActivity getInstance(){ return currentConversation; }
}
