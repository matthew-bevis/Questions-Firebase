package com.example.a21questionsfirebase;

import static java.lang.Math.floor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Random;

public class AskQuestion extends AppCompatActivity {

    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    Button askQ;
    Button addQ;
    Button clearQ;
    TextView q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        askQ = (Button) findViewById(R.id.askQ_act1);
        addQ = (Button) findViewById(R.id.addQ_act1);
        clearQ = (Button) findViewById(R.id.clearQ);
        q = (TextView) findViewById(R.id.q);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference("Questions");
        addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestionView();
            }
        });
        clearQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q.setText("");
            }
        });
        askQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int dbLen = (int)snapshot.getChildrenCount();
                        Random random = new Random();
                        int index = random.nextInt(dbLen);
                        String str = String.valueOf(index);
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                        int i = 0;
                        for(DataSnapshot childSnap : snapshot.getChildren()){
                            if(i == index) {
                                String question = childSnap.getValue(String.class);
                                q.setText(question);
                            }
                            i++;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });
    }
    public void addQuestionView(){
        Intent intent = new Intent(this, AddQuestion.class);
        startActivity(intent);
    }
}