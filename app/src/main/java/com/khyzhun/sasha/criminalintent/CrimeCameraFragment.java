package com.khyzhun.sasha.criminalintent;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

public class CrimeCameraFragment extends Fragment {

    private static final String TAG = "CrimeCameraFragment";
    public static final String EXTRA_PHOTO_FILENAME =
            "com.khyzhun.sasha.criminalintent.photo_filename";

    private Camera camera;
    private SurfaceView surfaceView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_camera, parent, false);
        wireTakePictureButton(view);

        surfaceView = (SurfaceView)view.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = surfaceView.getHolder();
        //Required for pre-Honeycomb compatibility.
        setSurfaceHolderType(holder);

        holder.addCallback(createSurfaceHolderCallback());

        return view;
    }

    private SurfaceHolder.Callback createSurfaceHolderCallback() {
        return new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (camera == null) {
                        return;
                    }

                    camera.setPreviewDisplay(holder);
                } catch(IOException ex) {
                    camera.release();
                    Log.e(TAG, "Error setting up preview display", ex);
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                if (camera == null) {
                    return;
                }

                camera.stopPreview();
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (camera == null) {
                    return;
                }

                setPreviewSize();

                try {
                    camera.startPreview();
                } catch (Exception ex) {
                    Log.e(TAG, "Could not start preview", ex);
                    camera.release();
                    camera = null;
                }
            }

            private void setPreviewSize() {
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size size = getBestSupportedSize(parameters.getSupportedPreviewSizes());
                parameters.setPreviewSize(size.width, size.height);
                camera.setParameters(parameters);
            }

            private Camera.Size getBestSupportedSize(List<Camera.Size> sizes) {
                Camera.Size bestSize = sizes.get(0);

                int largestArea = bestSize.width * bestSize.height;
                for (Camera.Size s : sizes) {
                    int area = s.width * s.height;
                    if (area > largestArea) {
                        bestSize = s;
                        largestArea = area;
                    }
                }

                return bestSize;
            }
        };
    }

    /**
     * Required for pre-Honeycomb compatibility.
     */
    @SuppressWarnings("deprecation")
    private void setSurfaceHolderType(SurfaceHolder holder) {
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @TargetApi(9)
    @Override
    public void onResume() {
        super.onResume();
        openCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private void openCamera() {
        if (AndroidVersionHelper.isGingerbreadOrHigher()) {
            camera = Camera.open(0);
        } else {
            camera = Camera.open();
        }
    }


    private void wireTakePictureButton(View view) {
        Button takePictureButton = (Button)view.findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }


}