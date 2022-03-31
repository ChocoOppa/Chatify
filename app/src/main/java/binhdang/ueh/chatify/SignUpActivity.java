package binhdang.ueh.chatify;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends Activity {
    private EditText inputUsername;
    private EditText inputDisplayName;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private Button signUpButton;
    private TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        SetUpViews();
    }

    private void SetUpViews(){
        inputUsername = (EditText) findViewById(R.id.input_signup_username);

        inputDisplayName = (EditText) findViewById(R.id.input_signup_display_name);

        inputPassword = (EditText) findViewById(R.id.input_signup_password);

        inputConfirmPassword = (EditText) findViewById(R.id.input_signup_confirm_password);


    }
}
