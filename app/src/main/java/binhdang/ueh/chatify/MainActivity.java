package binhdang.ueh.chatify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetUpViews();
    }

    private void SetUpViews(){
        listView = (ListView) findViewById(R.id.conversation_list);
        ConversationListAdapter adapter = new ConversationListAdapter(getApplicationContext());
        listView.setAdapter(adapter);

        menuButton = (ImageButton) findViewById(R.id.menu_button);
        menuButton.setOnClickListener(menuClicked);
    }

    View.OnClickListener menuClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(intent);
        }
    };
}