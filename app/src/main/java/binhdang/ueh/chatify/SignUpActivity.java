package binhdang.ueh.chatify;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private EditText inputUsername;
    private EditText inputDisplayName;
    private EditText inputPassword;
    private EditText inputConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        SetUpViews();
    }

    private void SetUpViews(){
        inputUsername = findViewById(R.id.input_signup_username);
        inputDisplayName = findViewById(R.id.input_signup_display_name);
        inputPassword = findViewById(R.id.input_signup_password);
        inputConfirmPassword = findViewById(R.id.input_signup_confirm_password);

        Button signUpButton = findViewById(R.id.signup_submit_button);
        signUpButton.setOnClickListener(signUpButtonClicked);

        TextView loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(loginButtonClicked);
    }

    View.OnClickListener signUpButtonClicked = view -> Submit();

    View.OnClickListener loginButtonClicked = view -> finish();

    private void Submit(){
        if(TextUtils.isEmpty(inputUsername.getText()) ||
                TextUtils.isEmpty(inputPassword.getText()) ||
                TextUtils.isEmpty(inputConfirmPassword.getText()) ||
                TextUtils.isEmpty(inputDisplayName.getText())){
            Toast.makeText(getApplicationContext(), "Please fill in all required fills!", Toast.LENGTH_SHORT).show();
        }
        else if (!(inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString()))){
            Toast.makeText(getApplicationContext(), "Password and confirmation aren't matched!", Toast.LENGTH_SHORT).show();
        }
        else{
            final int[] size = {0};
            db.collection("users")
                    .whereEqualTo("username", inputUsername.getText().toString()).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> size[0] = queryDocumentSnapshots.getDocuments().size())
                    .addOnFailureListener(e -> Log.w(TAG, "Error querying: ", e));
            if (size[0] > 0){
                Toast.makeText(getApplicationContext(), "Username already existed!", Toast.LENGTH_SHORT).show();
            }
            else {
                String pfpUri = "android.resource://" + getApplicationContext().getPackageName() + "/drawable/default_pfp";
                Uri file = Uri.parse(pfpUri);
                StorageReference storageRef = storage.getReference();
                StorageReference pfp = storageRef.child("pfp/" + inputUsername.getText().toString() + ".jpg");
                UploadTask uploadTask = pfp.putFile(file);
                uploadTask.addOnSuccessListener(taskSnapshot -> Log.d(TAG, "Profile picture uploaded successfully!")).
                        addOnFailureListener(e -> Log.d(TAG, "Profile picture failed to upload to storage!"));

                String username = inputUsername.getText().toString();
                String displayName = inputDisplayName.getText().toString();
                String password = inputPassword.getText().toString();

                Map<String, Object> user = new HashMap<>();
                user.put("username", username);
                user.put("displayName", displayName);
                user.put("password", password);

                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getApplicationContext(), "Successfully signed up!", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Log.w(TAG, "Error adding document", e);
                            Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }
}
