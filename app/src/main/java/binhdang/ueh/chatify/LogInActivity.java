package binhdang.ueh.chatify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInActivity extends Activity {
    private EditText usernameTextView;
    private EditText passwordTextView;
    private Button loginButton;
    private TextView signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SetUpViews();
    }

    private void SetUpViews(){
        usernameTextView = (EditText) findViewById(R.id.input_login_username);

        passwordTextView = (EditText) findViewById(R.id.input_login_password);

        loginButton = (Button) findViewById(R.id.login_submit_button);
        loginButton.setOnClickListener(loginSubmitButtonClicked);

        signupButton = (TextView) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(signUpButtonClicked);
    }

    View.OnClickListener loginSubmitButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Submit();
        }
    };

    View.OnClickListener signUpButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        }
    };

    private void Submit(){
        if(TextUtils.isEmpty(usernameTextView.getText()) || TextUtils.isEmpty(passwordTextView.getText())){
            Toast.makeText(getApplicationContext(), "Please fill in all required fills!", Toast.LENGTH_SHORT);
        }
        else{
            if(false){

            }
            else{
                finish();
            }
        }
    }
}
