package com.example.dell.minidubsmash;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;

import java.io.File;
import java.text.DecimalFormat;

import static android.R.attr.path;

public class CameraActivity extends AppCompatActivity {
    private final static int CAMERA_RQ = 6969;
    private final static int PERMISSION_RQ = 84;
    long n=0;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        String s=getIntent().getStringExtra("music");
        //Toast.makeText(this, s+"", Toast.LENGTH_SHORT).show();





        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission to save videos in external storage
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
        }

        File saveDir = null;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Only use external storage directory if permission is granted, otherwise cache directory is used by default
            saveDir = new File(Environment.getExternalStorageDirectory(), "Videos");
            saveDir.mkdirs();
        }
         mp = new MediaPlayer();

        try {

            mp.setDataSource(s+"");


            mp.prepare();
             n=mp.getDuration();

           // Toast.makeText(this, n+"", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();

        }
        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                mp.start();


            }
        }.start();

        new CountDownTimer(4000+n, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                mp.stop();


            }
        }.start();



        MaterialCamera materialCamera = new MaterialCamera(this)
                .saveDir(saveDir)
                .showPortraitWarning(false)
                .allowRetry(true)
                .defaultToFrontFacing(true)
                .allowRetry(true)
                .autoSubmit(false)

                .videoPreferredAspect(16f / 9f)
.audioDisabled(false)
                .labelConfirm(R.string.mcam_use_video)
                .qualityProfile(MaterialCamera.QUALITY_480P)
.countdownImmediately(true)
                .autoRecordWithDelaySec(4)
                .videoFrameRate(30)

                .maxAllowedFileSize(1024 * 1024 * 30) // 30Mb max file size
                .countdownSeconds((n/1000)+1)
                ;
        materialCamera.start(CAMERA_RQ);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {
                final File file = new File(data.getData().getPath());
                Toast.makeText(this, String.format("Saved to: %s, size: %s",
                        file.getAbsolutePath(), fileSize(file)), Toast.LENGTH_LONG).show();
                setResult(RESULT_OK,data);

mp.stop();
                mp.release();
                Uri selectedVideoUri = data.getData();
                //Intent viewCreatedVideo = new Intent(this, ViewCreatedVideo.class);
                //viewCreatedVideo.putExtra("PUT_EXTRA",selectedVideoUri.toString());
                //startActivity(viewCreatedVideo);


            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
        // This finishes this activity after execution and removes it from back stack
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            // Sample was denied WRITE_EXTERNAL_STORAGE permission
            Toast.makeText(this, "Videos will be saved in a cache directory instead of an external storage directory since permission was denied.", Toast.LENGTH_LONG).show();
        }
    }
    private String readableFileSize(long size) {
        if (size <= 0) return size + " B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private String fileSize(File file) {
        return readableFileSize(file.length());
    }






}
