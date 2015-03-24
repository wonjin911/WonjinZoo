package com.wonjin.wonjinzoo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {
    int user_idx = 0;

    // define variables for back key : 2 pressed end!
    private boolean isBackKeyPressed = false;             // flag
    private long currentTimeByMillis = 0;                     // calculate time interval

    private static final int MSG_TIMER_EXPIRED = 1;    // switch - key
    private static final int BACKKEY_TIMEOUT = 2;      // define interval
    private static final int MILLIS_IN_SEC = 1000;        // define millisecond
    // end of back key variable.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("동물 움짤 모음");
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#220A29"));

        // GET UUID
        String uuid = CreateDeviceUUID();

        // try login
        APICaller ac = new APICaller();
        user_idx = ac.user_login(uuid);

        // if new uuid, register
        if (user_idx == 0){
            // popup for username
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            intent.putExtra("uuid", uuid);
            startActivity(intent);
            finish();
        }

        Button bt = (Button) findViewById(R.id.button);
        bt.setText("전체");
        bt.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListActivity.class);
                i.putExtra("user_idx", user_idx);
                i.putExtra("type", 0);
                startActivity(i);
            }
        });
        Button bt1 = (Button) findViewById(R.id.button1);
        bt1.setText("강아지");
        bt1.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListActivity.class);
                i.putExtra("user_idx", user_idx);
                i.putExtra("type",1);
                startActivity(i);
            }
        });
        Button bt2 = (Button) findViewById(R.id.button2);
        bt2.setText("고양이");
        bt2.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListActivity.class);
                i.putExtra("user_idx", user_idx);
                i.putExtra("type",2);
                startActivity(i);
            }
        });
        Button bt3 = (Button) findViewById(R.id.button3);
        bt3.setText("맹수");
        bt3.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListActivity.class);
                i.putExtra("user_idx", user_idx);
                i.putExtra("type",3);
                startActivity(i);
            }
        });

        Button bt4 = (Button) findViewById(R.id.button4);
        bt4.setText("햄스터");
        bt4.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));
        bt4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListActivity.class);
                i.putExtra("user_idx", user_idx);
                i.putExtra("type",4);
                startActivity(i);
            }
        });

        Button bt5 = (Button) findViewById(R.id.button5);
        bt5.setText("새");
        bt5.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));
        bt5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListActivity.class);
                i.putExtra("user_idx", user_idx);
                i.putExtra("type",5);
                startActivity(i);
            }
        });

        Button bt_etc = (Button) findViewById(R.id.button_etc);
        bt_etc.setText("기타");
        bt_etc.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));
        bt_etc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListActivity.class);
                i.putExtra("user_idx", user_idx);
                i.putExtra("type",99);
                startActivity(i);
            }
        });

        Button bt_setting = (Button) findViewById(R.id.button_setting);
        bt_setting.setText("설정");
        bt_setting.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));
        bt_setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "준비중입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        AdView adView = (AdView) this.findViewById(R.id.adMob);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("0293D088028EED801C5232AA92507E76")
                .build();
        adView.loadAd(adRequest);

    }

    private String CreateDeviceUUID(){
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();

        return deviceId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
