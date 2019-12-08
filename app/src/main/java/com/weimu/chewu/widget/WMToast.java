package com.weimu.chewu.widget;

import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.Toast;

import com.weimu.chewu.AppData;
import com.weimu.chewu.R;

import es.dmoral.toasty.Toasty;


public class WMToast {


    public static void normal(CharSequence text) {
        custom(text);
    }

    public static void show(CharSequence text){
        int textColor = ContextCompat.getColor(AppData.getContext(), R.color.white);
        int backColor = ContextCompat.getColor(AppData.getContext(), R.color.black_alpha70);

        Toast toast = Toasty.custom(AppData.getContext(), text, null, textColor, backColor, Toast.LENGTH_SHORT, false, true);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void custom(CharSequence text) {
        int textColor = ContextCompat.getColor(AppData.getContext(), R.color.white);
        int backColor = ContextCompat.getColor(AppData.getContext(), R.color.black_alpha70);

        Toast toast = Toasty.custom(AppData.getContext(), text, null, textColor, backColor, Toast.LENGTH_SHORT, false, true);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static void longToast(CharSequence text) {
        int textColor = ContextCompat.getColor(AppData.getContext(), R.color.white);
        int backColor = ContextCompat.getColor(AppData.getContext(), R.color.black_alpha70);

        Toast toast = Toasty.custom(AppData.getContext(), text, null, textColor, backColor, Toast.LENGTH_LONG, false, true);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
