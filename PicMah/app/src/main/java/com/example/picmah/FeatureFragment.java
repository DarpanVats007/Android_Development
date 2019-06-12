package com.example.picmah;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.security.Permission;

public class FeatureFragment extends Fragment {

    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int PERMISSION_CODE = 1000;
    Button mCaptureBtn;
    ImageView mImageView;

    Uri image_uri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate (R.layout.fragment_feature,container,false);
        View view = inflater.inflate (R.layout.fragment_feature, container,false);

        mCaptureBtn = view.findViewById (R.id.capture_image);
        mImageView = view.findViewById (R.id.image_view);

        mCaptureBtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {




//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.CAMERA}, 0x123);
//                }

                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA)  == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                    // shoe popup to request permissions

                    requestPermissions (permission, PERMISSION_CODE);
                }

                else {

                    // Permissions Granted
                    openCamera();
                }
            }
        });

        return view;

    }

    private void openCamera(){

        // add permissions
        ContentValues values = new ContentValues ();
        values.put (MediaStore.Images.Media.TITLE, "New Pic");
        values.put (MediaStore.Images.Media.DESCRIPTION, "From Camera");
        Context applicationContext = MainActivity.getContextOfApplication();
//        applicationContext.getContentResolver();

        image_uri = applicationContext.getContentResolver ().insert (MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra (MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult (cameraIntent, IMAGE_CAPTURE_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED){
                    //PErmissions from popup granted
                    openCamera ();

                }
                else {
                    Toast.makeText (getActivity (), "PErmission denied",Toast.LENGTH_SHORT).show ();
                }

            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity ().RESULT_OK){
            mImageView.setImageURI (image_uri);

        }
    }
}
