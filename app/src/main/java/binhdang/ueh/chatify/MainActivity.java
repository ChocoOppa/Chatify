package binhdang.ueh.chatify;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ConversationBarDataQuery query;
    boolean firstTimeLaunched = true;
    private ListView listView;
    private ImageButton menuButton;
    List<ConversationBar> data;

    public static WeakReference<MainActivity> weakActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        query = new ConversationBarDataQuery(getApplicationContext());
        weakActivity = new WeakReference<>(MainActivity.this);

        if (CheckUserInSharedRef()) {
            CheckUserInSharedRefValid();
        }
        else {
            ReLogin();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(firstTimeLaunched){
            firstTimeLaunched = false;
            SetUpViews();
        }
        else {
            query.retrieveData();
        }
    }

    public static MainActivity getInstanceActivity() {
        return weakActivity.get();
    }

    private boolean CheckUserInSharedRef(){
        SharedPreferences sharedPref = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        return sharedPref.contains("username");
    }

    private void CheckUserInSharedRefValid(){
        SharedPreferences sharedPref = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        db.collection("users")
                .whereEqualTo("username", sharedPref.getString("username", ""))
                .whereEqualTo("password", sharedPref.getString("password", ""))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getData().size() <= 0){
                                ReLogin();
                            }
                        }
                    } else {
                        Log.d(TAG, "Error querying: ", task.getException());
                    }
                });
    }

    public void ReLogin(){
        SharedPreferences sharedPref = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().apply();
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(intent);
        Log.d("Query result", "Not yet logged in!");
    }

    private void SetUpViews(){
        List<ConversationBar> data = query.getData();
        listView = findViewById(R.id.conversation_list);
        ConversationListAdapter adapter = new ConversationListAdapter(getApplicationContext(), data);
        listView.setAdapter(adapter);
        menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(menuClicked);

        menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(menuClicked);
    }

    private void SetUpConversationsList(){
        listView = findViewById(R.id.conversation_list);
        ConversationListAdapter adapter = new ConversationListAdapter(getApplicationContext(), data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
            intent.putExtra("name", data.get(i).getTitle());
            intent.putExtra("conversation", data.get(i).getConversationName());

            startActivity(intent);
        });
    }

    View.OnClickListener menuClicked = view -> {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
    };

    public void UpdateMenu(){
        data = query.getData();
        SetUpConversationsList();
    }
}