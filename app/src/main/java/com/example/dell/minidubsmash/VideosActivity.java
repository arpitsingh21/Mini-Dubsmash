package com.example.dell.minidubsmash;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VideosActivity extends AppCompatActivity {
    static  RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> planetList=new ArrayList();
    File[] filelist;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        File dir = new File(Environment.getExternalStorageDirectory()+"/Videos/");
        filelist = dir.listFiles();

        if(filelist!=null) {
            String[] theNamesOfFiles = new String[filelist.length];
            for (int i = 0; i < theNamesOfFiles.length; i++) {
                planetList.add(filelist[i].getName());

            }

            adapter = new PlanetAdapter(planetList,getApplicationContext());
            recyclerView.setAdapter(adapter);
            Toast.makeText(this, filelist.length+"", Toast.LENGTH_SHORT).show();

        }
        else{

            Toast.makeText(this, "Add files in category", Toast.LENGTH_SHORT).show();
        }


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new   RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position)  {
                        // TODO Handle item click
 //                       Toast.makeText(VideosActivity.this, position+"", Toast.LENGTH_SHORT).show();


                        File file = new File(filelist[position].getPath());
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "video/*");
                        startActivity(intent);

                  }
                })
        );







    }
}
