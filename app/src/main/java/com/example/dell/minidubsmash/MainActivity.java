package com.example.dell.minidubsmash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import okio.BufferedSink;
import okio.Okio;

import static android.R.attr.path;

public class MainActivity extends AppCompatActivity {
    protected static final int REQ_CODE_PICK_SOUNDFILE = 0;
    StorageReference storage;
    String category;
    ArrayList<String> audiolist;
    private ProgressDialog progress;
    private ListView lv;
    File[] filelist;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button fab = (Button) findViewById(R.id.fab);
        category = getIntent().getStringExtra("category");
        storage = FirebaseStorage.getInstance().getReference();
        lv = (ListView) findViewById(R.id.list);
        audiolist = new ArrayList<String>();
        progress = new ProgressDialog(this);
         adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, audiolist);









        File dir = new File(Environment.getExternalStorageDirectory()+"/"+category+"/");
         filelist = dir.listFiles();

        if(filelist!=null) {
            String[] theNamesOfFiles = new String[filelist.length];
            for (int i = 0; i < theNamesOfFiles.length; i++) {
                audiolist.add(filelist[i].getName());

            }
            lv.setAdapter(adapter);

        }
        else{

            Toast.makeText(this, "Add files in category", Toast.LENGTH_SHORT).show();
        }

lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i=new Intent(MainActivity.this,CameraActivity.class);
        i.putExtra("music",filelist[position].getAbsolutePath());
        startActivity(i);

    }
});





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent;
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/mpeg");
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_audio_file_title)), REQ_CODE_PICK_SOUNDFILE);

            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_SOUNDFILE && resultCode == Activity.RESULT_OK) {
            if ((data != null) && (data.getData() != null)) {

                progress.setMessage("Uploading...");
                progress.show();
                progress.setCancelable(false);

                final Uri audioFileUri = data.getData();



                final String filpath=FileUtils.getPath(MainActivity.this,audioFileUri);



                //String file=RealPathUtils.getRealPathFromURI_API19(MainActivity.this,audioFileUri);;
                    final StorageReference filepath = storage.child(category).child(filpath.substring(filpath.lastIndexOf("/")+1));
                Toast.makeText(this, filpath+"", Toast.LENGTH_SHORT).show();
                    filepath.putFile(audioFileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(MainActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                            progress.dismiss();


try {

    File f=new File(Environment.getExternalStorageDirectory()+"/"+category+"/");
            if(!f.exists())
            {
                f.mkdirs();
            }
    File localFile = File.createTempFile(filpath.substring(filpath.lastIndexOf("/")+1),"",f);
    filepath.getFile(localFile)
            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Successfully downloaded data to local file
                    // ...
                    Toast.makeText(MainActivity.this, "Prepairing...", Toast.LENGTH_SHORT).show();

                    audiolist.add(filpath.substring(filpath.lastIndexOf("/")+1));

                    adapter.notifyDataSetChanged();

                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // Handle failed download
            // ...
            Toast.makeText(MainActivity.this, "File coudn't upload", Toast.LENGTH_SHORT).show();
        }
    });
}
catch(IOException e){e.printStackTrace();}


                        }

                    });


                   // Toast.makeText(this, audioFileUri + "", Toast.LENGTH_SHORT).show();
                    //  UploadTask audio=riversRef.putBytes(audioFileUri);
                    // Now you can use that Uri to get the file path, or upload it, ...
                }

                else{

                Toast.makeText(this, "No file selected or Not a audio file", Toast.LENGTH_SHORT).show();

            }
            }


        }


    }
