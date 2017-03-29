package com.phototell.util;

import android.view.Gravity;
import android.widget.Toast;

import com.phototell.PhotoTellApplication;

/**
 * Created by user on 29/03/2017.
 */

public class MessageUtility {

    public static void exception(Exception e){
        e.printStackTrace();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(PhotoTellApplication.getContext().getApplicationContext(),
                "exception:" + e.toString(), duration);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();
    }

    public static void message(String  message){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(PhotoTellApplication.getContext().getApplicationContext(),
                message, duration);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();
    }
}
