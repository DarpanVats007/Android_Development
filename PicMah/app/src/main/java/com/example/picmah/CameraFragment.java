package com.example.picmah;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;


public class CameraFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {

    Button mCaptureBtn;
    ImageView mImageView;
    CameraBridgeViewBase cameraBridgeViewBase;

    Mat mat1,mat2,mat3;
    BaseLoaderCallback baseLoaderCallback;

    public static final String TAG = "YOUR-TAG-NAME";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate (R.layout.fragment_camera,container,false);
        View view = inflater.inflate (R.layout.fragment_camera, container,false);
        Log.d("verify",String.valueOf(OpenCVLoader.initDebug()));
        if (OpenCVLoader.initDebug()) {

            Toast.makeText(getActivity ().getApplicationContext(), "OpenCV loaded sucessfully", Toast.LENGTH_SHORT).show();

        }
        else {

            Toast.makeText(getActivity ().getApplicationContext(), "Could not load OpenCV", Toast.LENGTH_SHORT).show();

        }

        cameraBridgeViewBase = (JavaCameraView) view.findViewById (R.id.myCameraView);
        cameraBridgeViewBase.setVisibility (JavaCameraView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener (this);
        baseLoaderCallback = new BaseLoaderCallback (getActivity().getApplicationContext()) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected (status);
                switch (status){

                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView ();
                        break;
                    default:
                        super.onManagerConnected (status);
                        break;
                }
            }
        };
//        baseLoaderCallback = new BaseLoaderCallback (this) {
//            @Override
//            public void onManagerConnected(int status) {
//                super.onManagerConnected (status);
//                switch (status){
//
//                    case BaseLoaderCallback.SUCCESS:
//                        cameraBridgeViewBase.enableView ();
//                        break;
//                    default:
//                        super.onManagerConnected (status);
//                        break;
//                }
//            }
//        };

        return view;

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mat1 = new Mat (width,height, CvType.CV_8UC4);
        mat2 = new Mat (width,height, CvType.CV_8UC4);
        mat3 = new Mat (width,height, CvType.CV_8UC4);


    }

    @Override
    public void onCameraViewStopped() {
        mat1.release ();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mat1 = inputFrame.rgba ();
        return mat1;
    }

    @Override
    public void onResume() {
        super.onResume ();

        Log.i(TAG, "Called onResume");

        Log.i(TAG, "Trying to load OpenCV library");
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, getActivity().getApplicationContext(), baseLoaderCallback))
        {
            Log.e(TAG, "Cannot connect to OpenCV Manager");
        }
//        if(!OpenCVLoader.initDebug ())
//        {
//            Toast.makeText (getActivity ().getApplicationContext (),"Problem in OpenCV",Toast.LENGTH_SHORT).show ();
//
//        }
//
//        else{
//
//            baseLoaderCallback.onManagerConnected (BaseLoaderCallback.SUCCESS);
//        }
    }


    @Override
    public void onPause() {
        super.onPause ();
        if(cameraBridgeViewBase!=null)
        {
            cameraBridgeViewBase.disableView ();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
        if(cameraBridgeViewBase!=null)
        {
            cameraBridgeViewBase.disableView ();

        }
    }
}
