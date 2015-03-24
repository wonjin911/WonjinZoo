package com.wonjin.wonjinzoo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class RegisterActivity extends ActionBarActivity {
    String user_name = "";
    String uuid = "";
    EditText et = null;

    // define variables for back key : 2 pressed end!
    private boolean isBackKeyPressed = false;             // flag
    private long currentTimeByMillis = 0;                     // calculate time interval

    private static final int MSG_TIMER_EXPIRED = 1;    // switch - key
    private static final int BACKKEY_TIMEOUT = 2;      // define interval
    private static final int MILLIS_IN_SEC = 1000;        // define millisecond
    // end of back key variable.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("사용하실 닉네임을 입력해주세요");

        Intent intent = getIntent();
        uuid = intent.getStringExtra("uuid");

        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount=0.7f;
        getWindow().setAttributes(layoutParams);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        getWindow().getAttributes().width = (int)(display.getWidth() * 0.8);
        getWindow().getAttributes().height = (int)(display.getHeight() * 0.3);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        setContentView(R.layout.user_reg);
        TextView ptv = (TextView)findViewById(R.id.popupTextView);
        ptv.setText("닉네임:");
        ptv.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));

        et = (EditText)findViewById(R.id.editText);

        Button bt = (Button)findViewById(R.id.regButton);
        bt.setText("확인");
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user_name = et.getText().toString();

                if (user_name.length() == 0){
                    Toast.makeText(RegisterActivity.this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
                }else {
                    APICaller ac = new APICaller();
                    ac.user_reg(uuid, user_name);

                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }


    public void onBackPressed(){
        if ( isBackKeyPressed == false ){
            // first click
            isBackKeyPressed = true;

            currentTimeByMillis = Calendar.getInstance().getTimeInMillis();
            Toast.makeText(this, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();

            startTimer();
        }else{
            // second click : 2초 이내면 종료! 아니면 아무것도 안한다.
            isBackKeyPressed = false;
            if ( Calendar.getInstance().getTimeInMillis() <= (currentTimeByMillis + (BACKKEY_TIMEOUT * MILLIS_IN_SEC)) ) {
                finish();
            }
        }
    }

    // startTimer : 2초의 시간적 여유를 가지게 delay 시킨다.
    private void startTimer(){
        backTimerHandler.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, BACKKEY_TIMEOUT * MILLIS_IN_SEC);
    }

    private Handler backTimerHandler = new Handler(){
        public void handleMessage(Message msg){
            switch( msg.what ){
                case MSG_TIMER_EXPIRED:{
                    isBackKeyPressed = false;
                }
                break;
            }
        }
    };
}

