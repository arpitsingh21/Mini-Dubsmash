package com.example.dell.minidubsmash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {
    Button en,hi,me,videos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
    hi=(Button)findViewById(R.id.button);
        en=(Button)findViewById(R.id.button3);
        me=(Button)findViewById(R.id.button2);
        hi.setOnClickListener(this);
        en.setOnClickListener(this);
        me.setOnClickListener(this);
videos=(Button)findViewById(R.id.videos);
videos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.button:

                Intent i=new Intent(this,MainActivity.class);
                i.putExtra("category","Hindi");
                startActivity(i);


                break;
            case R.id.button3:
                Intent j=new Intent(this,MainActivity.class);
                j.putExtra("category","Melody");
                startActivity(j);


                break;
            case R.id.button2 :
                Intent k=new Intent(this,MainActivity.class);
                k.putExtra("category","English");
                startActivity(k);

                break;
            case R.id.videos:
Intent f=new Intent(CategoryActivity.this,VideosActivity.class);
                startActivity(f);

        }


    }
}
