package com.wonjin.wonjinzoo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class GifActivity extends ActionBarActivity {
    int user_idx;
    int animal_idx;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_gif);

        Intent intent = getIntent();
        user_idx = intent.getIntExtra("user_idx",0);
        animal_idx = intent.getIntExtra("animal_idx",0);
        String detail = intent.getStringExtra("detail");
        String img_url = intent.getStringExtra("img_url");

        // LOAD GIF
        WebView webview;
        webview = (WebView)findViewById(R.id.gif_webview);
        webview.setBackgroundColor(Color.BLACK);
        webview.setHorizontalScrollBarEnabled(false);
        webview.setVerticalScrollBarEnabled(false);

        // add progress bar
        progress = (ProgressBar) findViewById(R.id.progressBar);
        webview.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress){
                progress.setVisibility(View.VISIBLE);
                if(newProgress >= 100){
                    progress.setVisibility(View.GONE);
                }
            }
        });

        WebSettings ws = webview.getSettings();
        ws.setSupportZoom(false);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        String htmlString = "<!DOCTYPE html><html>body style = \"text-align:center\"><img src=\"" + img_url + "\" width=\"100%\"></body></html>";
        webview.loadDataWithBaseURL(null, htmlString, "text/html", "UTF-8", null);
        //webview.loadUrl(img_url);
        //ImageController ic = new ImageController();
        //ic.LoadImage(webview, img_url);

        // SET TEXT
        TextView tv = (TextView) findViewById(R.id.gif_textview);
        tv.setText(detail);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));

        // GOOD BUTTON
        Button gb = (Button) findViewById(R.id.goodButton);
        gb.setText(Html.fromHtml("<FONT color=red>♥좋아요</FONT>"));
        gb.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));

        gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APICaller ac = new APICaller();
                int ret = ac.user_good(user_idx, animal_idx);
                if (ret == 1) {
                    Toast.makeText(GifActivity.this, "좋아요 하셨습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(GifActivity.this, "이미 좋아요 하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gif, menu);
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
}
