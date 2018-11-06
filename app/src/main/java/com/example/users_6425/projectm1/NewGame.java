package com.example.users_6425.projectm1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class NewGame extends AppCompatActivity {

    Button button;
    Button folders;
    ImageView imageView;
    static final int CAM_REQUEST = 1;
    static final int PICK_IMAGE = 100;
    Uri filepath=null;
    //String next;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        //Intent intent = getIntent();
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        button = (Button)findViewById(R.id.new_game);
        folders = (Button)findViewById(R.id.image);
        //imageView = (ImageView)findViewById(R.id.imageView);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                /*
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent,CAM_REQUEST);
                */

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager())!=null){
                    Environment.getDataDirectory();
                    startActivityForResult(takePictureIntent,CAM_REQUEST);
                }

            }
        });
        folders.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery,PICK_IMAGE);
            }
        });



    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == CAM_REQUEST && resultCode == RESULT_OK)){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            filepath= data.getData();

            System.out.println(imageBitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Intent intent = new Intent(this, ChunkedImageActivity.class);
            intent.putExtra("data",byteArray);
            intent.putExtra("filepath", filepath);
            System.out.println("filepath : "+filepath);
            startActivity(intent);
            //imageView.setImageBitmap(imageBitmap);
        }
        else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            Uri extras = data.getData();
            /*String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(extras, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            System.out.println("filepath : "+imagePath);
            System.out.println("filepath : "+extras);
            */
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), extras);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(imageBitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Intent intent = new Intent(this, ChunkedImageActivity.class);
            intent.putExtra("data",byteArray);
            intent.putExtra("filepath", extras);
            //System.out.println("filepath : "+filepath);
            startActivity(intent);
        }
    }



    public void onDestroy() {
        super.onDestroy();

    }
}
