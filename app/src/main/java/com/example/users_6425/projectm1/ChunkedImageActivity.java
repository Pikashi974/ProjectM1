package com.example.users_6425.projectm1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

//This activity will display the small image chunks into a grid view
public class ChunkedImageActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{
    private static final String DEBUG_TAG = "Move : ";
    ImageView imageView;
    private GestureDetectorCompat mDetector;
    public SharedPreferences settings;

    public void onCreate(Bundle bundle){

        super.onCreate(bundle);
        setContentView(R.layout.image_grid);
        Intent intent = getIntent();
        int[] ids = { R.id.imageView11, R.id.imageView12, R.id.imageView13,R.id.imageView14,
                R.id.imageView21, R.id.imageView22, R.id.imageView23,R.id.imageView24,
                R.id.imageView31, R.id.imageView32, R.id.imageView33,R.id.imageView34,
                R.id.imageView41, R.id.imageView42, R.id.imageView43,R.id.imageView44, };
        mDetector = new GestureDetectorCompat(this,this);

        Bundle extras = intent.getExtras();
        String choice = extras.getString("button");
        if(choice.equals("Continuer")){
            String filepath = extras.getString("filepath");
            Uri file = Uri.fromFile(new File(filepath));

            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            imageChuncks(filepath,ids,imageBitmap,extras, intent,true);

        }
        else if (choice.equals("Chosen")){
            String filepath = extras.getString("filepath");
            Uri file = Uri.fromFile(new File(filepath));

            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            imageChuncks(filepath,ids,imageBitmap,extras, intent,false);

        }
        else {
            byte[] byteArray = extras.getByteArray("data");

            Bitmap imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageChuncks(extras.getString("filepath"),ids,imageBitmap,extras, intent,false);

        }

    }

    public void imageChuncks(String path,int[] ids, Bitmap imageBitmap, Bundle extras, Intent intent, boolean C){
        //Getting the image chunks sent from the previous activity
        ArrayList<Bitmap> imageChunks = splitImage(imageBitmap,ids.length);
        //Getting the grid view and setting an adapter to it
        int[] listrand=new int[ids.length];
        System.out.println(C);
        if (C==false){
            ArrayList<Integer> rand= new ArrayList<>();
            if(extras.getIntArray("shuffle")==null){
                for (int i=0;i<ids.length;i++){
                    rand.add(i);
                }
                Collections.shuffle(rand);
            }
            else {rand = (ArrayList<Integer>) extras.get("shuffle");}

            for (int i=0;i<rand.size();i++){
                listrand[i]=rand.get(i);
            }

        }
        else{
            listrand = extras.getIntArray("listrand");
        }

        intent.putExtra("data",extras.getByteArray("data"));

        settings = getSharedPreferences ("rand", MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("rand");
        for (int i=0;i<ids.length;i++){
            ImageView mImg;
            mImg = (ImageView) findViewById(ids[i]);
            mImg.setImageBitmap(imageChunks.get(listrand[i]));
            editor.putInt("Place "+i, listrand[i]);
            //System.out.println("listrand["+i+"] = "+listrand[i]);
        }

        //System.out.println("filepath : "+ listrand[0]);
        // Apply the edits!
        Set<String> liste = new HashSet<>();
        for (int i=0;i<listrand.length;i++){
            liste.add(""+listrand[i]);
            //System.out.println("liste["+i+"] = "+listrand[i]);
        }
        //editor.putStringSet("rand",liste);
        editor.putString("path",path);
        editor.commit();

        System.out.println("Filepath : "+ settings.getString("path",""));
    }

    private ArrayList<Bitmap> splitImage(Bitmap image, int chunkNumbers) {

        //For the number of rows and columns of the grid to be displayed
        int rows, cols;

        //For height and width of the small image chunks
        int chunkHeight, chunkWidth;

        //To store all the small image chunks in bitmap format in this list
        ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);

        //Getting the scaled bitmap of the source image
        //BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = image;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        rows = cols = (int) Math.sqrt(chunkNumbers);
        chunkHeight = bitmap.getHeight() / rows;
        chunkWidth = bitmap.getWidth() / cols;

        //xCoord and yCoord are the pixel positions of the image chunks
        int yCoord = 0;
        for (int x = 0; x < rows; x++) {
            int xCoord = 0;
            for (int y = 0; y < cols; y++) {
                chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }
        return chunkedImages;
    }

    /**********************
     * Gestion des gestes *
     **********************/

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());

        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: " + e1.toString() + e2.toString());
        return true;
    }
}

/*
 SharedPreferences prefs = getPreferences(MODE_PRIVATE);
int[] list = new int[10];
StringBuilder str = new StringBuilder();
for (int i = 0; i < list.length; i++) {
    str.append(list[i]).append(",");
}
prefs.edit().putString("string", str.toString());
Get the string and parse it using StringTokenizer:

String savedString = prefs.getString("string", "");
StringTokenizer st = new StringTokenizer(savedString, ",");
int[] savedList = new int[10];
for (int i = 0; i < 10; i++) {
    savedList[i] = Integer.parseInt(st.nextToken());
}
 */