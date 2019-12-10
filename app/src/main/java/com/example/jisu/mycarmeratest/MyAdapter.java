package com.example.jisu.mycarmeratest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {
    private int layout;
    private ArrayList<DateSaveList> list = new ArrayList<>();

    public MyAdapter(int layout, ArrayList<DateSaveList> list) {
        this.layout = layout;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.CustomViewHolder customViewHolder, int i) {
        customViewHolder.recycText.setText(list.get(i).getText());
       Bitmap bitmap=BitmapFactory.decodeFile(list.get(i).getImagepsth());

        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(list.get(i).getImagepsth());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation;//방향
        int exifDegres; //각도
        if (exifInterface != null) {
            exifOrientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegres = exiforToDe(exifOrientation);
        }else{
            exifDegres=0;
        }
        Bitmap bitmapTeep=rotate(bitmap,exifDegres);
        customViewHolder.recycimageview.setImageBitmap(bitmapTeep);


    }

    @Override
    public int getItemCount() { return (list != null) ? list.size() : 0; }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView recycimageview;
        TextView recycText;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            recycimageview = itemView.findViewById(R.id.recycimageview);
            recycText = itemView.findViewById(R.id.recycText);
        }
    }
    private int exiforToDe(int exifOrientation) {
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
        }
        return 0;
    }
    private Bitmap rotate(Bitmap bitmap, int exifDegres) {
        Matrix matrix=new Matrix();
        matrix.postRotate(exifDegres);
        Bitmap teepre=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return teepre;
    }
}
