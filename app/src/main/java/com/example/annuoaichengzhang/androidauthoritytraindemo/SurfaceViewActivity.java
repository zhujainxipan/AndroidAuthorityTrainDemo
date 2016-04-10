package com.example.annuoaichengzhang.androidauthoritytraindemo;

import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.List;

public class SurfaceViewActivity extends AppCompatActivity {
    private Camera camera;
    private SurfaceView mSurfaceView;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSurfaceView = (SurfaceView) findViewById(R.id.camera_sufaceview);
        mBtn = (Button) findViewById(R.id.take_btn);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            camera = Camera.open(0);
        } else {
            camera = Camera.open();
        }




        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback() {
                               @Override
                               public void surfaceCreated(SurfaceHolder holder) {
                                   try {
                                       if (camera != null) {
                                           camera.setPreviewDisplay(holder);
                                       }
                                   } catch (IOException e) {

                                   }
                               }

                               @Override
                               public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                                   if (camera == null) {
                                       return;
                                   }
                                   Camera.Parameters parameters = camera.getParameters();
                                   Camera.Size size = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
                                   parameters.setPreviewSize(size.width, size.height);
                                   camera.setParameters(parameters);
                                   try {
                                       camera.startPreview();
                                   } catch (Exception e) {
                                       camera.release();
                                       camera = null;
                                   }

                               }

                               @Override
                               public void surfaceDestroyed(SurfaceHolder holder) {
                                   if (camera != null) {
                                       camera.stopPreview();
                                       camera.release();
                                       camera = null;
                                   }
                               }
                           }

        );
    }


    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height) {
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Camera.Size size : sizes) {
            int area = size.width + size.height;
            if (area > largestArea) {
                bestSize = size;
                largestArea = area;
            }
        }
        return bestSize;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
