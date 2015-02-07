package com.wonjin.wonjinzoo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;


public class GifActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        Intent intent = getIntent();
        String img_url = intent.getStringExtra("img_url");

        TextView tv = (TextView) findViewById(R.id.gif_textview);
        tv.setText(img_url);

        WebView webview;
        webview = (WebView)findViewById(R.id.gif_webview);
        webview.setBackgroundColor(Color.BLACK);

        WebSettings ws = webview.getSettings();

        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        //ws.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        //ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        ImageController ic = new ImageController();
        ic.LoadImage(webview, img_url);
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
