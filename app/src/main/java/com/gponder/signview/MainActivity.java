package com.gponder.signview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SignView signView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signView = findViewById(R.id.sign_view);

    }

    public void save(View view){
        String path = signView.save();
        Toast.makeText(this,path+"",Toast.LENGTH_SHORT).show();
    }

    public void clear(View view){
        signView.clear();
    }
}
