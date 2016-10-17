package com.example.userpc.form_sutdio;

public class NavItem {
    String mTitle;
    int type;
    int set_id;
    int ques_id;
 
    public NavItem(String title, int icon,int set,int quesid) {
        mTitle = title;        
        type = icon;  
        set_id=set;
        ques_id=quesid;
    }
}