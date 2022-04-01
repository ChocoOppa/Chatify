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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
    private ListView listView;
    private ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (CheckUserInSharedRef() && CheckUserInSharedRefValid()) {
            SetUpViews();
        }
        else {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear().apply();
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
        }
    }

    private boolean CheckUserInSharedRef(){
        return sharedPref.contains("username");
    }

    private boolean CheckUserInSharedRefValid(){
        final int[] size = {0};
        db.collection("users")
                .whereEqualTo("username", sharedPref.getString("username", ""))
                .whereEqualTo("password", sharedPref.getString("password", ""))
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
        if (size[0] <= 0){
            return false;
        }
        else{
            return true;
        }
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