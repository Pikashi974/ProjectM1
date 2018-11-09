package com.example.users_6425.projectm1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    String file;
    int[] liste;
    public SharedPreferences settings ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

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
            settings = getSharedPreferences("rand", MODE_WORLD_READABLE);
            file = settings.getString("path","");
            liste = getSetStr(settings);
            if(liste!=null){
                //System.out.println("file : "+ file);
                Intent intent = new Intent(this, ChunkedImageActivity.class);
                intent.putExtra("filepath",file);
                intent.putExtra("button",next);
                intent.putExtra("listrand",liste);
                startActivity(intent);
            }
            else{
                Toast toast = Toast.makeText(this, "Pas de partie sauvegardée", Toast.LENGTH_LONG);
                System.out.println("Pas de partie sauvegardée");
                System.out.println("Filepath : "+file);
            }
        } else {
            next = "MainActivity";
        }
        System.out.println(next);

    }

    public int[] getSetStr(SharedPreferences settings){
        liste=new int[16];
        for (int i=0;i<16;i++){
            liste[i]= settings.getInt("Place "+i,0);
        }
        return liste;
    }

    private static byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }


    public void onDestroy() {
        super.onDestroy();
    }
}
