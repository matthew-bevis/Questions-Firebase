package com.example.a21questionsfirebase;

import static android.widget.Toast.makeText;

import static com.google.common.base.Predicates.equalTo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class AddQuestion extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference mDatabase = database.getReference("Questions");
    Button askQ;
    Button addQ;
    Button clearDB;
    EditText qDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        askQ = (Button) findViewById(R.id.askQ_act2);
        addQ = (Button) findViewById(R.id.addQ_act2);
        clearDB = (Button) findViewById(R.id.clear);
        qDB = (EditText) findViewById(R.id.qDB);
        clearDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.removeValue();
            }
        });
        askQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askQuestionView();
            }
        });
        addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quest = qDB.getText().toString();
                DatabaseReference qChild = mDatabase.push();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String nwQuest = quest.replaceAll(" ","");
                        boolean inDb = false;
                        for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String str = childSnapshot.getValue(String.class);
                            if (str.equalsIgnoreCase(quest)){
                                inDb = true;
                                qDB.setTextColor(Color.RED);
                                Toast.makeText(getApplicationContext(), "Question already exists.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (quest.equals("") == false && inDb == false) {
                            if (nwQuest.length() < 120) {
                                qChild.setValue(quest);
                                qDB.setTextColor(Color.BLACK);
                                Toast.makeText(getApplicationContext(), "Added to database.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                qDB.setTextColor(Color.RED);
                                Toast.makeText(getApplicationContext(), "Question cannot be longer than 120 characters.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            qDB.setTextColor(Color.RED);
                            if(quest.equals("")) {
                                Toast.makeText(getApplicationContext(), "Question cannot be empty.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }
    public void askQuestionView(){
        Intent intent = new Intent(this, AskQuestion.class);
        startActivity(intent);
    }
}