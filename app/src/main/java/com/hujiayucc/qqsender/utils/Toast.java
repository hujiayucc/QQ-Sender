package com.hujiayucc.qqsender.utils;

import android.annotation.SuppressLint;
import com.hujiayucc.qqsender.Applications;

public class Toast {
    private static final int LONG = 1;
    private static final int SHORT = 0;

    @SuppressLint("WrongConstant")
    public static void Long(String message) {
        android.widget.Toast.makeText(Applications.Companion.getContext(), message, LONG).show();
    }

    @SuppressLint("WrongConstant")
    public static void Short(String message) {
        android.widget.Toast.makeText(Applications.Companion.getContext(), message, SHORT).show();
    }
}
