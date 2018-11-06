package com.example.users_6425.projectm1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

//This activity will display the small image chunks into a grid view
public class ChunkedImageActivity extends AppCompatActivity {

    ImageView imageView;

    public void onCreate(Bundle bundle){

        super.onCreate(bundle);
        setContentView(R.layout.image_grid);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        byte[] byteArray = extras.getByteArray("data");

        Bitmap imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        int[] ids = { R.id.imageView11, R.id.imageView12, R.id.imageView13,R.id.imageView14,
                R.id.imageView21, R.id.imageView22, R.id.imageView23,R.id.imageView24,
                R.id.imageView31, R.id.imageView32, R.id.imageView33,R.id.imageView34,
                R.id.imageView41, R.id.imageView42, R.id.imageView43,R.id.imageView44, };
        //Getting the image chunks sent from the previous activity
        ArrayList<Bitmap> imageChunks = splitImage(imageBitmap,ids.length);

        //Getting the grid view and setting an adapter to it
        ArrayList<Integer> rand= new ArrayList<>();
        if(extras.getIntArray("shuffle")==null){
            for (int i=0;i<ids.length;i++){
                rand.add(i);
            }
            Collections.shuffle(rand);
        }
        else {rand = (ArrayList<Integer>) extras.get("shuffle");}

        Object[] listrand = rand.toArray();


        intent.putExtra("shuffle", listrand);
        intent.putExtra("data",extras.getByteArray("data"));

        System.out.println(imageChunks.size());

        for (int i=0;i<ids.length;i++){
                ImageView mImg;
                mImg = (ImageView) findViewById(ids[i]);
                mImg.setImageBitmap(imageChunks.get((Integer) listrand[i]));
        }

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

        /* Now the chunkedImages has all the small image chunks in the form of Bitmap class.
         * You can do what ever you want with this chunkedImages as per your requirement.
         * I pass it to a new Activity to show all small chunks in a grid for demo.
         * You can get the source code of this activity from my Google Drive Account.
         */

        return chunkedImages;

        //Start a new activity to show these chunks into a grid
        //Intent intent = new Intent(this, ChunkedImageActivity.class);
        //intent.putParcelableArrayListExtra("image chunks", chunkedImages);
        //startActivity(intent);
    }
}
