package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText enterTitle;
    private EditText enterThoughts;
    private Button saveButton;

    private Button showDataButton;

    private TextView receiveTitle;

    private TextView receiveThoughts;

    private static final String TAG = "MainActivity";

    //Keys for the database
    public static final String KEY_TITLE = "title";
    public static final String KEY_THOUGHTS = "thoughts";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterTitle = findViewById(R.id.enterTitle);
        enterThoughts = findViewById(R.id.enterThoughts);
        saveButton = findViewById(R.id.saveButton);
        showDataButton = findViewById(R.id.showDataButton);
        receiveTitle = findViewById(R.id.receiveTitle);
        receiveThoughts = findViewById(R.id.receiveThoughts);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference journalRef = db.document("Journal/First Thoughts");

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = enterTitle.getText().toString().trim();
                        String thought = enterThoughts.getText().toString().trim();

                        Map<String, Object> data = new HashMap<>();
                        data.put(KEY_TITLE, title);
                        data.put(KEY_THOUGHTS, thought);

                        journalRef.set(data)
                                .addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG)
                                            .show();
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding the data", e);
                                    }
                                });

                        // Add a new document with a generated ID
//                        Task<Void> voidTask = db.collection("Journal")
//                                .document("First Thoughts")
//                                .set(data)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.w(TAG, "Error adding the data", e);
//                                    }
//                                });

                    }


                });

                showDataButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        journalRef.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            String title = documentSnapshot.getString(KEY_TITLE);
                                            String thought = documentSnapshot.getString(KEY_THOUGHTS);

                                            receiveTitle.setText(title);
                                            receiveThoughts.setText(thought);
                                        }
                                        else {
                                            Toast.makeText(MainActivity.this, "No Data Exists!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"onFailure:" + e.toString());
                                    }
                                });
                    }
                });

    }
}