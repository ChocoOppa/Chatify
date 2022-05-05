package binhdang.ueh.chatify;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ChangePasswordActivity extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText changePassword;
    EditText changePasswordConfirm;
    Button changePasswordButton;
    ImageButton backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        SetUpViews();
    }

    private void SetUpViews() {
        TextView title = findViewById(R.id.titleTextView);
        title.setText(R.string.change_password);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(backButtonClicked);

        changePassword = findViewById(R.id.change_password);
        changePasswordConfirm = findViewById(R.id.change_password_confirm);

        changePasswordButton = findViewById(R.id.change_password_submit_button);
        changePasswordButton.setOnClickListener(newPass);
    }

    View.OnClickListener backButtonClicked = view -> finish();

    View.OnClickListener newPass = view -> {
            String pass = changePassword.getText().toString();
            String passConfirm = changePasswordConfirm.getText().toString();
            if(!pass.equals("") && !passConfirm.equals("")) {
                if(pass.equals(passConfirm)) {
                    SharedPreferences sharedPref = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
                    db.collection("users")
                            .whereEqualTo("username", sharedPref.getString("username", ""))
                            .get()
                            .addOnCompleteListener(task -> {
                                for(QueryDocumentSnapshot doc : task.getResult()) {
                                    String docID = doc.getId();
                                    db.collection("users")
                                            .document(docID)
                                            .update("password", String.valueOf(changePassword.getText()));
                                }
                            });
                } else {
                    Log.w("PASS", "Passwords do not match! " + changePassword.getText() + " " + changePasswordConfirm.getText());
                }
            } else {
                Log.w("PASS", "Pass is empty!");
            }
    };
}
