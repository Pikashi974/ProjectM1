package com.example.users_6425.projectm1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        try {
            filepath = (extras.getString("filepath"));
        }
        catch (Exception e){
            filepath = "";
        }



    }
    public void onClick(View view) throws ClassNotFoundException {
        String next;
        if (view.getId() == R.id.new_game) {
            next = "NewGame";
            Intent intent = new Intent(this, NewGame.class);
            intent.putExtra("shuffle", (int[]) null);
            intent.putExtra("data",(byte[]) null);
            startActivity(intent);
        } else if (view.getId() == R.id.image) {
            next = "Continuer";
            if (filepath==""){
                Toast toast = Toast.makeText(this, "Pas de partie sauvegardée", Toast.LENGTH_LONG);
                System.out.println("Pas de partie sauvegardée");
            }
            else {
                Intent intent = new Intent(this, ChunkedImageActivity.class);
                startActivity(intent);
            }
        } else {
            next = "MainActivity";
        }
        System.out.println(next);

    }
    public void onDestroy() {
        super.onDestroy();
    }
}
