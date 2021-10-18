package com.rydz.driver.CommonUtils.CustomTextUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.rydz.driver.R;


public class CustomAutoCompleteTextView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    String customFont;

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);

    }




    private void init(AttributeSet attrs) {

        if (attrs != null) {

            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            String fontName = a.getString(R.styleable.CustomTextView_fontName);

           /* try {

                if (fontName != null) {

                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);

                    setTypeface(myTypeface);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }*/
            a.recycle();

        }
    }



}