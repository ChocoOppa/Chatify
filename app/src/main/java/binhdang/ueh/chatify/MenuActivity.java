package binhdang.ueh.chatify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MenuActivity extends Activity {
    private ImageButton backButton;
    private LinearLayout editProfileButton;
    private LinearLayout changePasswordButton;
    private LinearLayout addFriendButton;
    private LinearLayout logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SetUpViews();
    }

    private void SetUpViews(){
        TextView topTextView = (TextView)findViewById(R.id.titleTextView);
        topTextView.setText("Menu");
        ImageView img = (ImageView) findViewById(R.id.profile_picture);
        Picasso.get().load("https://i.pinimg.com/736x/4f/db/1c/4fdb1c761d2a2e604012123e981a0a1c.jpg").resize(400, 400).centerCrop().transform(new CropCircleTransformation()).into(img);

        backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnClickListener(backButtonClicked);

        editProfileButton = (LinearLayout) findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(editProfileButtonClicked);

        changePasswordButton = (LinearLayout) findViewById(R.id.change_password_button);
        changePasswordButton.setOnClickListener(changePasswordButtonClicked);

        addFriendButton = (LinearLayout) findViewById(R.id.add_friend_button);
        addFriendButton.setOnClickListener(addFriendButtonClicked);

        logoutButton = (LinearLayout) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(logoutButtonClicked);
    }

    View.OnClickListener editProfileButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    View.OnClickListener changePasswordButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    View.OnClickListener addFriendButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    View.OnClickListener logoutButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    View.OnClickListener backButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
}
