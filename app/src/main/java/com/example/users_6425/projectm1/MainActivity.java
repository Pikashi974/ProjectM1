package com.example.users_6425.projectm1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View view) throws ClassNotFoundException {
        String next;
        if (view.getId() == R.id.new_game) {
            next = "NewGame";
            Intent intent = new Intent(this, NewGame.class);
            startActivity(intent);
            finish();
        } else if (view.getId() == R.id.UnderScore) {
            next = "UnderScore";
        } else {
            next = "MainActivity";
        }
        System.out.println(next);

    }
    public void onDestroy() {
        super.onDestroy();
    }
}
