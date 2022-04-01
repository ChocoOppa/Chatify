package binhdang.ueh.chatify;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MenuActivity extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SetUpViews();
    }

    private void SetUpViews(){
        final DocumentSnapshot[] user = new DocumentSnapshot[1];
        db.collection("users")
                .whereEqualTo("username", sharedPref.getString("username", ""))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        user[0] = queryDocumentSnapshots.getDocuments().get(0);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error querying: ", e);
                    }
                });

        TextView topTextView = findViewById(R.id.titleTextView);
        topTextView.setText(R.string.menu);

        String pfpUrl = user[0].get("pfpSrc").toString();
        StorageReference pfp = storage.getReference(pfpUrl);
        ImageView img = findViewById(R.id.profile_picture);
        pfp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString())
                        .resize(400, 400)
                        .centerCrop()
                        .transform(new CropCircleTransformation()).into(img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                Log.d(TAG, "Failed to load profile picture: " + e);
            }
        });

        TextView displayName = findViewById(R.id.profile_display_name);
        displayName.setText(user[0].get("displayName").toString());

        TextView username = findViewById(R.id.profile_username);
        username.setText(user[0].get("username").toString());

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(backButtonClicked);

        LinearLayout editProfileButton = findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(editProfileButtonClicked);

        LinearLayout changePasswordButton = findViewById(R.id.change_password_button);
        changePasswordButton.setOnClickListener(changePasswordButtonClicked);

        LinearLayout addFriendButton = findViewById(R.id.add_friend_button);
        addFriendButton.setOnClickListener(addFriendButtonClicked);

        LinearLayout logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(logoutButtonClicked);
    }

    View.OnClickListener editProfileButtonClicked = view -> {

    };

    View.OnClickListener changePasswordButtonClicked = view -> {

    };

    View.OnClickListener addFriendButtonClicked = view -> {

    };

    View.OnClickListener logoutButtonClicked = view -> {

    };

    View.OnClickListener backButtonClicked = view -> finish();
}
