package com.logbook.logbookapp;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


/**
 * Created by me on 8/14/2016.
 */
public class AutoHideKeyboardEditText extends EditText {
    public AutoHideKeyboardEditText(Context context) {
        super(context);
        init();
    }
    public AutoHideKeyboardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public AutoHideKeyboardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("hidekey", "in onFocusChange "+v.getResources().getResourceEntryName(v.getId()));
                if (!hasFocus) {
                    Log.d("hidekey", "going to hide "+v.getResources().getResourceEntryName(v.getId()));
                    hideKeyboard(v);
                }
            }
        });
    }
    private void hideKeyboard(View view) {
        Log.d("hidekey", "hide keyboard " + view.getResources().getResourceEntryName(view.getId()));
        InputMethodManager inputMethodManager = (InputMethodManager)
                view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
