package com.example.jisu.mycarmeratest;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageview;
    private Button btn, btnNext;
    private String imageFilepath;
    private Uri phortUri;
    static final int RE = 672;
    public static Bitmap imagesave = null;
    private ArrayList<DateSaveList> list = new ArrayList<>();
    private String imageFile;
    private EditText edttext;
    private MyDB mydb;
    private SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageview = findViewById(R.id.imageview);
        btn = findViewById(R.id.btn);
        btnNext = findViewById(R.id.btnNext);
        edttext = findViewById(R.id.edttext);
        mydb = new MyDB(this);
        selectDB();
        TedPermission.with(getApplicationContext()).setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                toastDispaly("on");
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                toastDispaly("off");
            }
        })
                .setRationaleMessage("카메라권한 필요합니다.")
                .setDeniedMessage("거부")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
        btn.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    public void toastDispaly(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            //1번 카메라를 띄워라
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (intent.resolveActivity(getPackageManager()) != null) {
                File file = null;
                //파일명을 만들어야됨 파일명.jpg
                try {
                    file = imageFileSave();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file != null) {
                    phortUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), file);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, phortUri);
                startActivityForResult(intent, RE);
            }
        } else if (v.getId() == R.id.btnNext) {
            Intent intent = new Intent(MainActivity.this, Subimage.class);
            intent.putExtra("list", list);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilepath);
            ExifInterface exifInterface = null;
            //속성을 체크해야된다.
            try {
                exifInterface = new ExifInterface(imageFilepath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int exifOrientation;//방향
            int exifDegres; //각도
            if (exifInterface != null) {
                exifOrientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegres = exiforToDe(exifOrientation);
            } else {
                exifDegres = 0;
            }
            Bitmap bitmapTeep = rotate(bitmap, exifDegres);
            imageview.setImageBitmap(bitmapTeep);
            sqlDB = mydb.getWritableDatabase();
            sqlDB.execSQL("INSERT INTO carmeraTBL VALUES('" + imageFilepath + "','" +
                    edttext.getText().toString() + "');");
            sqlDB.close();
            list.add(new DateSaveList(imageFilepath, edttext.getText().toString()));
        }
    }

    private Bitmap rotate(Bitmap bitmap, int exifDegres) {
        Matrix matrix = new Matrix();
        matrix.postRotate(exifDegres);
        Bitmap teepre = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return teepre;
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

    private File imageFileSave() throws IOException {
        SimpleDateFormat date = new SimpleDateFormat("yyyymmdd_HHmmss");
        // 현재 날짜를 이름에 넣어서 중복을 없애기
        String string = date.format(new Date());
        imageFile = "test_" + string + "_";
        //외부장치의 디렉토리
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFile, ".jpg", directory);
        imageFilepath = image.getAbsolutePath();
        return image;
    }
    private void selectDB(){
        Cursor cursor=null;
        sqlDB = mydb.getWritableDatabase();
        cursor = sqlDB.rawQuery("SELECT * FROM carmeraTBL;", null);
        list.removeAll(list);
        while (cursor.moveToNext()) {
//            DateSaveList datalist = new DateSaveList();
//            String image = cursor.getString(0);
//            String text = cursor.getString(1);
//            datalist.setImagepsth(image);
//            datalist.setText(text);
            list.add(new DateSaveList(cursor.getString(0), cursor.getString(1)));
        }
        cursor.close();
        sqlDB.close();
    }
}
