package com.deyizai.taptapdetaildemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created  on 2017-9-4.
 *
 * @author cdy
 * @version 1.0.0
 */

public class SecondActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Button b = (Button) findViewById(R.id.sec_but);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SecondActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
