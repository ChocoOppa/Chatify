package binhdang.ueh.chatify;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView listView;
    private ImageButton menuButton;

    public static WeakReference<MainActivity> weakActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        SetUpViews();
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
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().size() > 0){
                                    SetUpViews();
                                }
                                else{
                                    ReLogin();
                                }
                            }
                        } else {
                            Log.d(TAG, "Error querying: ", task.getException());
                        }
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