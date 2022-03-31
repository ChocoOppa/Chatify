package binhdang.ueh.chatify;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LogInActivity extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText inputUsername;
    private EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SetUpViews();
    }

    private void SetUpViews(){
        inputUsername = findViewById(R.id.input_login_username);
        inputPassword = findViewById(R.id.input_login_password);

        Button loginButton = findViewById(R.id.login_submit_button);
        loginButton.setOnClickListener(loginSubmitButtonClicked);

        TextView signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(signUpButtonClicked);
    }

    View.OnClickListener loginSubmitButtonClicked = view -> Submit();

    View.OnClickListener signUpButtonClicked = view -> {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    };

    private void Submit(){
        if(TextUtils.isEmpty(inputUsername.getText()) || TextUtils.isEmpty(inputPassword.getText())){
            Toast.makeText(getApplicationContext(), "Please fill in all required fills!", Toast.LENGTH_SHORT).show();
        }
        else{
            final int[] size = {0};
            db.collection("users")
                    .whereEqualTo("username", inputUsername.getText())
                    .whereEqualTo("password", inputPassword.getText())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            size[0] = queryDocumentSnapshots.getDocuments().size();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error querying: ", e);
                        }
                    });
            if (size[0] > 0){
                Toast.makeText(getApplicationContext(), "Username already existed!", Toast.LENGTH_SHORT).show();
            }
            else{
                finish();
            }
        }
    }
}
