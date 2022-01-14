package com.example.piceditor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView IVPreviewImage;

    ImageView img_gallery;

    private int REQUEST_CODE_PERMISSIONS = 101;
    private static final int PICK_IMAGE_REQUEST = 102;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IVPreviewImage=(ImageView) findViewById(R.id.IVPreviewImage);
        img_gallery=(ImageView) findViewById(R.id.img_gallery);
        getSupportActionBar().hide();
        if(allPermissionsGranted()){

        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        IVPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toast for click confirmation or jump to new Activity
                Intent intent=new Intent(MainActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });


        img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toast for click confirmation or jump to new Activity
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setAction(intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //noinspection deprecation
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
//                startActivity(intent);
            }
        });




    }


    public String getRealPathFromUri(Context context, Uri contentUri)
    {

        Cursor cursor=getContentResolver().query(contentUri,null,null,null,null);

        cursor.moveToFirst();
        String document_id=cursor.getString(0);
        document_id=document_id.substring(document_id.lastIndexOf(":")+1);

        cursor.close();

        cursor=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Images.Media._ID
                +" = ? ",new String[]{document_id},null);

        cursor.moveToFirst();
        String path=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode==PICK_IMAGE_REQUEST)
       {
           if(data.getData()!=null)
           {
//               Toast.makeText(this,data.getData().toString(),Toast.LENGTH_SHORT).show();
               Uri uri  =data.getData();
               String path=getRealPathFromUri(getApplicationContext(),uri);
               Toast.makeText(getApplicationContext(),path+"",Toast.LENGTH_SHORT).show();
               Intent intent2= new Intent(MainActivity.this,ShowPhotoActivity.class);
               intent2.putExtra("path",path+"");
               startActivity(intent2);


           }
       }
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){

            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}