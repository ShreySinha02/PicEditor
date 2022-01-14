package com.example.piceditor;

import static com.example.piceditor.ShowPhotoActivity.RotateBitmap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;

import java.io.File;

public class EditPhotoActivity2 extends AppCompatActivity {
    ImageView imgedit;
    String path="";
    Uri inputImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo2);

        imgedit=(ImageView) findViewById(R.id.imgedit);
        path=getIntent().getExtras().getString("path");

        File imgfile=new File(path);

        if(imgfile.exists())
        {
            Bitmap myBitmap= BitmapFactory.decodeFile(imgfile.getAbsolutePath());

            imgedit.setImageBitmap(RotateBitmap(myBitmap,90));
        }


        inputImageUri=Uri.fromFile(new File(path));
        edit_trial();
    }


    public void edit_trial()
    {
        Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);

        dsPhotoEditorIntent.setData(inputImageUri);


        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "piceditor");

        int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};

        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

        startActivityForResult(dsPhotoEditorIntent, 200);




    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case 200:

                    Uri outputUri = data.getData();

                    // handle the result uri as you want, such as display it in an imageView;

                     imgedit.setImageURI(outputUri);

                    break;

            }

        }
    }
}