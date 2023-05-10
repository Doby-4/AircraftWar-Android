package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.R;
import edu.hitsz.application.Game;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;

    private int gameType=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button medium_btn = findViewById(R.id.medium_btn);
        Button easy_btn = findViewById(R.id.easy_btn);
        Button hard_btn = findViewById(R.id.hard_btn);
        Switch SoundEffect_switch = findViewById(R.id.SoundEffect_switch);

        getScreenHW();

        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        medium_btn.setOnClickListener(view -> {
            gameType=1;
            intent.putExtra("gameType",gameType);
            startActivity(intent);
        });

        easy_btn.setOnClickListener(view -> {
            gameType =2;
            intent.putExtra("gameType",gameType);
            startActivity(intent);
        });

        hard_btn.setOnClickListener(view -> {
            gameType =3;
            intent.putExtra("gameType",gameType);
            startActivity(intent);
        });

        SoundEffect_switch.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                Log.d(TAG,"SoundEffect_switch is on");
                Game.soundEffectEnable = true;
            }else{
                Log.d(TAG,"SoundEffect_switch is off");
                Game.soundEffectEnable = false;
            }
        });
    }
    public void getScreenHW(){
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        WINDOW_WIDTH = dm.widthPixels;
        //窗口高度
        WINDOW_HEIGHT = dm.heightPixels;

        Log.i(TAG, "screenWidth : " + WINDOW_WIDTH + " screenHeight : " + WINDOW_HEIGHT);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}