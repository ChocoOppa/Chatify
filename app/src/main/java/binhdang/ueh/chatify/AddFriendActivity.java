package binhdang.ueh.chatify;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AddFriendActivity extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    String resultUserName;
    String resultDisplayName;

    Button searchButton;
    EditText inputNameEditText;
    ImageView friendToAddPfp;
    TextView friendToAddDisplayName;
    TextView friendToAddUsername;
    TextView addFriendButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        SetUpViews();
    }

    private void SetUpViews(){
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(backButtonClicked);

        searchButton = findViewById(R.id.search_friend_button);
        searchButton.setOnClickListener(SearchButtonClicked);

        inputNameEditText = findViewById(R.id.input_name_edit_text);

        friendToAddPfp = findViewById(R.id.friend_to_add_profile_picture);
        friendToAddDisplayName = findViewById(R.id.friend_to_add_display_name);
        friendToAddUsername = findViewById(R.id.friend_to_add_username);

        addFriendButton = findViewById(R.id.add_friend_button);
        addFriendButton.setOnClickListener(AddFriendButtonClicked);
    }

    View.OnClickListener backButtonClicked = view -> {
        finish();
    };

    View.OnClickListener SearchButtonClicked = view -> {
        String inputName = inputNameEditText.getText().toString();
        if(!inputName.equals("")){
            db.collection("users")
                    .whereEqualTo("username", inputName)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot doc: task.getResult()) {
                                Map result = doc.getData();
                                UpdateFriendToAddProfile(result.get("displayName").toString(), result.get("username").toString());
                            }
                        }
                    });
        }
    };

    private void UpdateFriendToAddProfile(String displayName, String username){
        SharedPreferences sharedPref = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        StorageReference storageRef = storage.getReference();
        String pfpSrc = "pfp/" + username + ".jpg";
        StorageReference pfp = storageRef.child(pfpSrc);
        pfp.getDownloadUrl().addOnSuccessListener(uri ->
                Picasso.get().load(uri.toString())
                        .resize(400, 400).centerCrop()
                        .transform(new CropCircleTransformation()).into(friendToAddPfp));

        resultDisplayName = displayName;
        friendToAddDisplayName.setText(displayName);
        resultUserName = username;
        friendToAddUsername.setText(username);

        addFriendButton.setVisibility(View.VISIBLE);
    }

    View.OnClickListener AddFriendButtonClicked = view -> {
        SharedPreferences sharedPref = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String currentUsername = sharedPref.getString("username", "");

        db.collection("users")
                .whereEqualTo("username", currentUsername)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for(QueryDocumentSnapshot doc: task.getResult()) {
                            Map result = doc.getData();
                            AddRelationshipToDb(currentUsername, result.get("displayName").toString());
                        }
                    }
                });
    };

    private void AddRelationshipToDb(String myUName, String myDName){
        Map<String, Object> relationship1 = new HashMap<>();
        relationship1.put("user1", myUName);
        relationship1.put("user2", resultUserName);

        Map<String, Object> relationship2 = new HashMap<>();
        relationship2.put("user1", resultUserName);
        relationship2.put("user2", myUName);

        db.collection("relationships")
                .add(relationship1)
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                });

        db.collection("relationships")
                .add(relationship2)
                .addOnSuccessListener(documentReference -> {
                    AddConversationToDb(myUName, myDName);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                });
    }

    private void AddConversationToDb(String myUName, String myDName){
        Map<String, Object> conversation = new HashMap<>();
        String conName = myUName + resultUserName + System.currentTimeMillis();
        conversation.put("conversation", conName);
        conversation.put("type", "private");

        db.collection("conversations")
                .add(conversation)
                .addOnSuccessListener(documentReference -> {
                    AddConversationMembersToDb(conName, myUName);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                });
    }

    private void AddConversationMembersToDb(String conName, String myUName){
        Map<String, Object> member1 = new HashMap<>();
        member1.put("conversation", conName);
        member1.put("member", myUName);

        Map<String, Object> member2 = new HashMap<>();
        member2.put("conversation", conName);
        member2.put("member", resultUserName);

        db.collection(("conversationsMembers"))
                .add(member1)
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                });

        db.collection(("conversationsMembers"))
                .add(member2)
                .addOnSuccessListener(documentReference -> {
                    AddConversationBarToDb(conName, myUName);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                });
    }

    private void AddConversationBarToDb(String conName, String myUName){
        Map<String, Object> bar1 = new HashMap<>();
        bar1.put("conversation", conName);
        bar1.put("forUser", myUName);
        bar1.put("otherUser", resultUserName);
        bar1.put("lastConversationMsg", "Say Hello to your new friend!");
        bar1.put("lastConversationTime", System.currentTimeMillis());

        Map<String, Object> bar2 = new HashMap<>();
        bar2.put("conversation", conName);
        bar2.put("forUser", resultUserName);
        bar2.put("otherUser", myUName);
        bar2.put("lastConversationMsg", "Say Hello to your new friend!");
        bar2.put("lastConversationTime", System.currentTimeMillis());

        db.collection(("conversationsBars"))
                .add(bar1)
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                });

        db.collection(("conversationsBars"))
                .add(bar2)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Successfully added user!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                });
    }
}
