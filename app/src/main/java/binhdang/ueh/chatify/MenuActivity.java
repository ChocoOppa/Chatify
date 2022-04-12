package binhdang.ueh.chatify;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MenuActivity extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SetUpViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateProfileInfo();
    }

    private void SetUpViews(){
        TextView topTextView = findViewById(R.id.titleTextView);
        topTextView.setText(R.string.menu);

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

        UpdateProfileInfo();
    }

    private void UpdateProfileInfo(){
        SharedPreferences sharedPref = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        final DocumentSnapshot[] user = new DocumentSnapshot[1];
        db.collection("users")
                .whereEqualTo("username", sharedPref.getString("username", ""))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TextView displayName = findViewById(R.id.profile_display_name);
                                displayName.setText(document.getData().get("displayName").toString());

                                TextView username = findViewById(R.id.profile_username);
                                username.setText(document.getData().get("username").toString());
                            }
                        } else {
                            Log.d(TAG, "Error querying: " + task.getException());
                        }
                    }
                });

        StorageReference storageRef = storage.getReference();
        ImageView img = findViewById(R.id.profile_picture);
        String pfpSrc = "pfp/" + sharedPref.getString("username", "") + ".jpg";
        StorageReference pfp = storageRef.child(pfpSrc);
        pfp.getDownloadUrl().addOnSuccessListener(uri ->
                Picasso.get().load(uri.toString())
                        .resize(400, 400).centerCrop()
                        .transform(new CropCircleTransformation()).into(img));
    }

    View.OnClickListener editProfileButtonClicked = view -> {
        Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
        startActivity(intent);
    };

    View.OnClickListener changePasswordButtonClicked = view -> {
        Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
        startActivity(intent);
    };

    View.OnClickListener addFriendButtonClicked = view -> {

    };

    View.OnClickListener logoutButtonClicked = view -> {
        MainActivity.getInstanceActivity().ReLogin();
        finish();
    };

    View.OnClickListener backButtonClicked = view -> finish();
}
