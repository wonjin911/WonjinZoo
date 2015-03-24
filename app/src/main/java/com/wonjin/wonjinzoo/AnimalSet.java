package com.wonjin.wonjinzoo;

public class AnimalSet {
    int idx;
    int type;
    String img_url;
    String detail;
    int view_cnt;
    int good_cnt;
    int flag;

    public AnimalSet(int _idx, int _type, String _img_url, String _detail, int _view_cnt, int _good_cnt, int _flag){
        idx = _idx;
        type = _type;
        img_url = _img_url;
        detail = _detail;
        view_cnt = _view_cnt;
        good_cnt = _good_cnt;
        flag = _flag;
    }
}
