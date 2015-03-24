package com.wonjin.wonjinzoo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/*
    0: 전체
    1: 강아지
    2; 고양이
    3: 맹수
    4. 햄스터
    5. 새
    99. 기타
*/

public class ListActivity extends ActionBarActivity {
    int user_idx;
    int type;
    ArrayList<AnimalSet> best_as_list;
    ArrayList<AnimalSet> as_list;
    bestAdapter best_adapter;
    animalAdapter adapter;
    ListView best_lv;
    ListView lv;
    Button btnLoadMore;
    int pageNum = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#220A29"));

        Intent intent = getIntent();
        user_idx = intent.getIntExtra("user_idx",0);
        type = intent.getIntExtra("type",0);

        setTitle(showType(type));

        TextView btv = (TextView)findViewById(R.id.bestTextView);
        btv.setText(Html.fromHtml("<FONT color=#ffffff>BEST 5</FONT>"));
        btv.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));

        // Get Initial List by calling API
        APICaller ac = new APICaller();
        String xml = "";

        // get Best List
        xml = ac.getBestList(type);
        best_as_list = new ArrayList<AnimalSet>();
        best_as_list = ac.parser(xml);

        best_lv = (ListView)findViewById(R.id.bestListView);
        best_lv.setVerticalScrollBarEnabled(false);

        // SET adapter
        best_adapter = new bestAdapter(this, R.layout.best_row, best_as_list);
        best_lv.setAdapter(best_adapter);

        best_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AnimalSet as = (AnimalSet) best_lv.getItemAtPosition(position);

                int animal_idx = as.idx;
                String detail = as.detail;
                String img_url = as.img_url;
                Intent i = new Intent(ListActivity.this, GifActivity.class);
                i.putExtra("user_idx", user_idx);
                i.putExtra("animal_idx", animal_idx);
                i.putExtra("detail", detail);
                i.putExtra("img_url", img_url);
                startActivity(i);
            }
        });

        // get List
        xml = ac.getAnimalList(0, type, pageNum + 1);
        as_list = new ArrayList<AnimalSet>();
        as_list = ac.parser(xml);

        lv = (ListView)findViewById(R.id.listView);

        // SET footer
        if (as_list.size() > pageNum) {
            as_list.remove(as_list.size()-1);

            btnLoadMore = new Button(this);
            btnLoadMore.setText("더 보기");
            btnLoadMore.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));
            btnLoadMore.setBackgroundColor(Color.parseColor("#ffffff"));
            btnLoadMore.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addItems(pageNum + 1);
                }
            });
            lv.addFooterView(btnLoadMore);
        }else{
            AdView adView = (AdView) this.findViewById(R.id.adMob);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("0293D088028EED801C5232AA92507E76")
                    .build();
            adView.loadAd(adRequest);
        };

        // SET adapter
        adapter = new animalAdapter(this, R.layout.list_row, as_list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AnimalSet as = (AnimalSet) lv.getItemAtPosition(position);

                int animal_idx = as.idx;
                String detail = as.detail;
                String img_url = as.img_url;
                Intent i = new Intent(ListActivity.this, GifActivity.class);
                i.putExtra("user_idx", user_idx);
                i.putExtra("animal_idx", animal_idx);
                i.putExtra("detail", detail);
                i.putExtra("img_url", img_url);
                startActivity(i);
            }
        });

    }

    private String showType(int type){
        String ret = "";
        switch (type){
            case 0 : ret = "전체"; break;
            case 1 : ret = "강아지"; break;
            case 2 : ret = "고양이"; break;
            case 3 : ret = "맹수"; break;
            case 4 : ret = "햄스터"; break;
            case 5 : ret = "새"; break;
            default : ret = "기타"; break;
        }
        return ret;
    }

    private void addItems(int num) {
        APICaller ac = new APICaller();
        String xml = "";
        int idx=0;

        for(int i=0;i<as_list.size();i++){
            if (i==0){
                idx = as_list.get(i).idx;
            }
            else if (idx > as_list.get(i).idx){
                idx = as_list.get(i).idx;
            }
        }
        ArrayList<AnimalSet> as_list_tmp = new ArrayList<AnimalSet>();
        xml = ac.getAnimalList(idx, type, num);
        as_list_tmp = ac.parser(xml);
        // SET footer
        if (as_list_tmp.size() <= pageNum) {
            btnLoadMore.setVisibility(View.GONE);

            AdView adView = (AdView) this.findViewById(R.id.adMob);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("0293D088028EED801C5232AA92507E76")
                    .build();
            adView.loadAd(adRequest);
        }else {
            as_list_tmp.remove(as_list_tmp.size() - 1);
        };
        // SET adapter
        as_list.addAll(as_list_tmp);
        adapter.notifyDataSetChanged();
    }


    private class bestAdapter extends ArrayAdapter<AnimalSet>{
        private ArrayList<AnimalSet> items;

        public bestAdapter(Context context, int textViewResourceId, ArrayList<AnimalSet> items){
            super(context, textViewResourceId, items);
            this.items = items;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View v = convertView;
            if (v == null){
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.best_row, null);
            }


            AnimalSet as = items.get(position);
            if (as != null){
                TextView tv = (TextView) v.findViewById(R.id.typeView);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                TextView gcv = (TextView) v.findViewById(R.id.goodCntView);
                TextView nt = (TextView) v.findViewById(R.id.newTextView);

                tv.setText("[" + showType(as.type) + "]");
                tv.setTextColor(Color.RED);
                tv.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding Bold.ttf"));
                bt.setText(abbreviate(as.detail, 20));
                bt.setTextColor(Color.BLACK);
                bt.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding Bold.ttf"));

                if (as.flag == 1) {
                    nt.setText(Html.fromHtml("<FONT color=red>new</FONT>"));
                    nt.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));
                }else{
                    nt.setText("");
                }

                gcv.setText(Html.fromHtml("<FONT color=red>♥" + as.good_cnt + "</FONT>"));
                gcv.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));
            }
            return v;
        }

        public String abbreviate(String str, int maxWidth) {
            if (null == str) {
                return null;
            }
            if (str.length() <= maxWidth) {
                return str;
            }
            return str.substring(0, maxWidth-3) + "...";
        }
    }

    private class animalAdapter extends ArrayAdapter<AnimalSet>{
        private ArrayList<AnimalSet> items;

        public animalAdapter(Context context, int textViewResourceId, ArrayList<AnimalSet> items){
            super(context, textViewResourceId, items);
            this.items = items;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View v = convertView;
            if (v == null){
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_row, null);
            }
            if ((position % 2) == 0) {
                v.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }else{
                v.setBackgroundColor(Color.parseColor("#E6E0F8"));
            }

            AnimalSet as = items.get(position);
            if (as != null){
                TextView tv = (TextView) v.findViewById(R.id.typeView);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                TextView nt = (TextView) v.findViewById(R.id.newTextView);
                TextView gcv = (TextView) v.findViewById(R.id.goodCntView);

                tv.setText("[" + showType(as.type) + "]");
                tv.setTextColor(Color.RED);
                tv.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding Bold.ttf"));
                bt.setText(abbreviate(as.detail,20));
                bt.setTextColor(Color.BLACK);
                bt.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));

                if (as.flag == 1) {
                    nt.setText(Html.fromHtml("<FONT color=red>new</FONT>"));
                    nt.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));
                }else{
                    nt.setText("");
                }

                gcv.setText(Html.fromHtml("<FONT color=red>♥" + as.good_cnt + "</FONT>"));
                gcv.setTypeface(Typeface.createFromAsset(getAssets(), "UhBee puding.ttf"));
            }
            return v;
        }

        public String abbreviate(String str, int maxWidth) {
            if (null == str) {
                return null;
            }
            if (str.length() <= maxWidth) {
                return str;
            }
            return str.substring(0, maxWidth-3) + "...";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

