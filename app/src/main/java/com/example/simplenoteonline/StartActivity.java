package com.example.simplenoteonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {


    private Button btnReg;
    private Button btnLog;

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnReg=findViewById(R.id.start_reg_btn);
        btnLog=findViewById(R.id.start_log_btn);

        fAuth=FirebaseAuth.getInstance();

        updateUI();
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }


    private void register(){

        Intent regIntent=new Intent(StartActivity.this,RegisterActivity.class);
        startActivity(regIntent);
    }

    private void login(){
        Intent logIntent=new Intent(StartActivity.this,LoginActivity.class);
        startActivity(logIntent);
        finish();
    }


    private void updateUI(){
        if(fAuth.getCurrentUser()!=null){

            Log.i("StartActivity","fAuth != null");
            Intent startIntent=new Intent(StartActivity.this,MainActivity.class);
            startActivity(startIntent);
            finish();
        }else {

            Log.i("StartActivity","fAuth == null");

        }
    }
}
