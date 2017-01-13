package com.letv.android.bezierutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private BezierView bezierView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bezierView = (BezierView) findViewById(R.id.bezier);
    }


    public void clear(View v){
        bezierView.clear();
    }

}
