package binhdang.ueh.chatify;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ViewConversationInfoActivity extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    ImageButton backButton;

    ImageView conversationPicture;
    TextView conversationDisplayName;
    TextView friendUsername;
    Button removeConversation;

    String conversation;
    String type;

    String otherUsernameInPrivate;

    //boolean removeButtonPressed = false;

    //boolean[] finishedRemovePrivateCon = new boolean[] {false, false, false, false, false, false};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetUpViews();

        conversation = getIntent().getStringExtra("conversation");
        type = getIntent().getStringExtra("type");
        RetrieveConversationProfile();
    }

    private void SetUpViews(){
        TextView title = findViewById(R.id.titleTextView);
        title.setText(R.string.info);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(backButtonClicked);

        // removeConversation = findViewById(R.id.remove_conversation_button);
        // removeConversation.setOnClickListener(removeConversationButtonClicked);
    }

    View.OnClickListener backButtonClicked = view -> {
        finish();
    };

/*    View.OnClickListener removeConversationButtonClicked = view -> {
        SharedPreferences sharedPref = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        if(!removeButtonPressed){
            removeButtonPressed = true;
            ConversationActivity.getInstance().finish();
            if (type.equals("private")){
                db.collection("chats")
                        .whereEqualTo("conversation", conversation)
                        .get()
                        .addOnCompleteListener(task -> {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                db.collection("chats").document(document.getId()).delete()
                                        .addOnCompleteListener(task1 -> {
                                            finishedRemovePrivateCon[0] = true;
                                            FinishRemovePrivateCon();
                                        });
                            }
                        });
                db.collection("conversationsBars")
                        .whereEqualTo("conversation", conversation)
                        .get()
                        .addOnCompleteListener(task -> {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                db.collection("conversationsBars").document(document.getId()).delete()
                                        .addOnCompleteListener(task1 -> {
                                            finishedRemovePrivateCon[1] = true;
                                            FinishRemovePrivateCon();
                                        });
                            }
                        });
                db.collection("conversationsMembers")
                        .whereEqualTo("conversation", conversation)
                        .get()
                        .addOnCompleteListener(task -> {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                db.collection("conversationsMembers").document(document.getId()).delete()
                                        .addOnCompleteListener(task1 -> {
                                            finishedRemovePrivateCon[2] = true;
                                            FinishRemovePrivateCon();
                                        });
                            }
                        });
                db.collection("conversations")
                        .whereEqualTo("conversation", conversation)
                        .get()
                        .addOnCompleteListener(task -> {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                db.collection("conversations").document(document.getId()).delete()
                                        .addOnCompleteListener(task1 -> {
                                            finishedRemovePrivateCon[3] = true;
                                            FinishRemovePrivateCon();
                                        });
                            }
                        });
                db.collection("relationships")
                        .whereEqualTo("user1", sharedPref.getString("username", ""))
                        .whereEqualTo("user2", otherUsernameInPrivate)
                        .get()
                        .addOnCompleteListener(task -> {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                db.collection("relationships").document(document.getId()).delete()
                                        .addOnCompleteListener(task1 -> {
                                            finishedRemovePrivateCon[4] = true;
                                            FinishRemovePrivateCon();
                                        });
                            }
                        });
                db.collection("relationships")
                        .whereEqualTo("user1", otherUsernameInPrivate)
                        .whereEqualTo("user2", sharedPref.getString("username", ""))
                        .get()
                        .addOnCompleteListener(task -> {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                db.collection("relationships").document(document.getId()).delete()
                                        .addOnCompleteListener(task1 -> {
                                            finishedRemovePrivateCon[5] = true;
                                            FinishRemovePrivateCon();
                                        });
                            }
                        });

            }
        }
    };

    private void FinishRemovePrivateCon(){
        for (int i = 0; i < finishedRemovePrivateCon.length; i++){
            if (!finishedRemovePrivateCon[i]){
               return;
            }
        }
        Toast.makeText(getApplicationContext(), "Successfully remove user!", Toast.LENGTH_SHORT).show();
        finish();
    }*/

    private void RetrieveConversationProfile(){
        SharedPreferences sharedPref = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        if (type.equals("private")){
            //removeConversation.setText(R.string.remove_this_friend);

            db.collection("conversationsMembers")
                    .whereEqualTo("conversation", conversation)
                    .whereNotEqualTo("member", sharedPref.getString("username", ""))
                    .get()
                    .addOnCompleteListener(task -> {
                       for (QueryDocumentSnapshot document : task.getResult()){
                           Map conMem = document.getData();
                           otherUsernameInPrivate = conMem.get("member").toString();
                           UpdateConversationProfileIfPrivate();
                       }
                    });
        }
    }

    private void UpdateConversationProfileIfPrivate(){
        db.collection("users")
                .whereEqualTo("username", otherUsernameInPrivate)
                .get()
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Map user = document.getData();
                        conversationDisplayName.setText(user.get("displayName").toString());
                        friendUsername.setText(otherUsernameInPrivate);
                        StorageReference storageRef = storage.getReference();
                        String pfpSrc = "pfp/" + otherUsernameInPrivate+ ".jpg";
                        StorageReference pfp = storageRef.child(pfpSrc);
                        pfp.getDownloadUrl().addOnSuccessListener(uri ->
                                Picasso.get().load(uri.toString())
                                        .resize(400, 400).centerCrop()
                                        .transform(new CropCircleTransformation()).into(conversationPicture));
                    }
                });
    }
}
