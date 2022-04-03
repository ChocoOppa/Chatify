package binhdang.ueh.chatify;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Map;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class EditProfileActivity extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    EditText editDisplayName;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        SetUpViews();
    }

    private void SetUpViews(){
        TextView topTextView = findViewById(R.id.titleTextView);
        topTextView.setText(R.string.edit_profile);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(backButtonClicked);

        Button saveButton = findViewById(R.id.edit_submit_button);
        saveButton.setOnClickListener(saveButtonClicked);

        editDisplayName = findViewById(R.id.edit_profile_display_name);

        img = findViewById(R.id.edit_profile_picture);
        img.setOnClickListener(editProfilePictureClicked);

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
                                editDisplayName.setText(document.getData().get("displayName").toString());
                                UpdatePfp();
                            }
                        } else {
                            Log.d(TAG, "Error querying: " + task.getException());
                        }
                    }
                });
    }

    private void UpdatePfp(){
        SharedPreferences sharedPref = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        StorageReference storageRef = storage.getReference();
        String pfpSrc = "pfp/" + sharedPref.getString("username", "") + ".jpg";
        StorageReference pfp = storageRef.child(pfpSrc);
        pfp.getDownloadUrl().addOnSuccessListener(uri ->
                Picasso.get().load(uri.toString())
                        .resize(400, 400).centerCrop()
                        .transform(new CropCircleTransformation()).into(img));
    }

    View.OnClickListener backButtonClicked = view -> {
        finish();
    };

    View.OnClickListener editProfilePictureClicked = view -> {
        OpenFileChooser();
    };

    View.OnClickListener saveButtonClicked = view -> {
        Submit();
    };

    private void OpenFileChooser(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null){
            SetUpNewPfp(data.getData());
        }
    }

    private void SetUpNewPfp(Uri muri){
        SharedPreferences sharedPref = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        StorageReference storageRef = storage.getReference();
        String pfpSrc = "pfp/" + sharedPref.getString("username", "") + ".jpg";
        StorageReference pfp = storageRef.child(pfpSrc);
        UploadTask uploadTask = pfp.putFile(muri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Log.d(TAG, "Profile picture uploaded successfully!");
            UpdatePfp();
        }).addOnFailureListener(e -> Log.d(TAG, "Profile picture failed to upload to storage!"));
    }

    private void Submit(){

    }
}
