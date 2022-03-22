package com.sarthaksavasil.todoapp;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class AddNewTask extends AppCompatActivity {
    EditText editText;
    Button saveBtn;

    FirebaseFirestore db;
    DocumentReference docRef;
    DocumentReference servicesRef;


    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    String personEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        editText = findViewById(R.id.task_edittext);
        saveBtn = findViewById(R.id.save_btn);
        getGoogleUserData();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }
    void saveTask(){
        String taskText = editText.getText().toString();
        taskText = taskText.trim();
        if(taskText==NULL ||taskText == "") {
            Toast.makeText(AddNewTask.this, "Task Cannot be blank", Toast.LENGTH_SHORT);
            return;
        }

        db = FirebaseFirestore.getInstance();
        docRef = db.collection(personEmail).document();
        updateDatabse(taskText);
    }

    private void getGoogleUserData() {

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            personEmail = acct.getEmail();
        }
    }

    void updateDatabse(String taskText){
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    services = (Map<String, Object>) document.get("services");
//                    Log.d("XYZ", services + "");
//
//                }

                Map<String, String> data = new HashMap<>();
                data.put("Task",taskText);

                docRef.set(data, SetOptions.merge());
                finish();
            }
            else{
                Toast.makeText(AddNewTask.this, "Some Error Occured", Toast.LENGTH_SHORT);
            }
        });
    }
}