package com.rydz.driver.CommonUtils.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class MySharedPreferences {
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.rydz.driver";
    private SharedPreferences.Editor prefereceEditor;

    public MySharedPreferences(Context context)
    {
        mPreferences = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        prefereceEditor = mPreferences.edit();
    }

    public void setBooleanValue(String key,Boolean value)
    {
        prefereceEditor.putBoolean(key,value).commit();
    }

    public Boolean getBooleanValue(String key)
    {
        Boolean isNull=mPreferences.getBoolean(key,false)!=false;
        return isNull ? mPreferences.getBoolean(key,false) :false;
    }

    public void setStringValue(String key,String value)
    {
        prefereceEditor.putString(key,value).commit();
    }

    public String getStringValue(String key)
    {
        Boolean isNull=mPreferences.getString(key,"")!=null;
      return isNull ? mPreferences.getString(key,"") : "";
    }

    public void setIntValue(String key,int value)
    {
        prefereceEditor.putInt(key,value).commit();
    }

    public int getIntValue(String key)
    {
        Boolean isNull=mPreferences.getInt(key,0)!=0;
        return isNull ? mPreferences.getInt(key,0) : 0;

    }

    public void setFloatValue(String key,Float value)
    {
        prefereceEditor.putFloat(key,value).commit();
    }

    public Float getFloatValue(String key)
    {
        Boolean isNull=mPreferences.getFloat(key,0)!=0;
        return isNull ? mPreferences.getFloat(key,0) : 0;
    }

    public  void clearSharedPrefrences() {
        prefereceEditor.clear().commit();
    }
}
