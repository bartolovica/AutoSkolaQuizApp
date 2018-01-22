package hr.fesb.autoskola250.androidonlinequizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

import hr.fesb.autoskola250.androidonlinequizapp.Common.Common;
import hr.fesb.autoskola250.androidonlinequizapp.Model.Question;

public class Start extends AppCompatActivity {

    Button btnPlay;

    FirebaseDatabase database;
    DatabaseReference questions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        loadQuestion(Common.categoryID);

        btnPlay = (Button)findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this,Playing.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadQuestion(String categoryID) {

        //prvo, počistimo listu ako imamo starih pitanja
        if(Common.questionList.size() > 0)
            Common.questionList.clear();

        questions.orderByChild("categoryID").equalTo(categoryID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            Question ques = postSnapshot.getValue(Question.class);
                            Common.questionList.add(ques);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        //Random lista ptanja
        Collections.shuffle(Common.questionList);

    }
}
