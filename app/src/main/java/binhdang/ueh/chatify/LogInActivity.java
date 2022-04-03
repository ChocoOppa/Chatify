package binhdang.ueh.chatify;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
            db.collection("users")
                    .whereEqualTo("username", inputUsername.getText().toString())
                    .whereEqualTo("password", inputPassword.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getData().size() > 0){
                                        SharedPreferences sharedRef = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedRef.edit();
                                        editor.putString("username", inputUsername.getText().toString());
                                        editor.putString("password", inputPassword.getText().toString());
                                        editor.apply();
                                        Toast.makeText(getApplicationContext(), "Welcome back!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Username or password invalid!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error querying: ", task.getException());
                            }
                        }
                    });
        }
    }
}
