package com.phototell.ui.views;

import android.content.Context;
import android.support.annotation.NonNull;

import com.phototell.common.ui.CustomView;

import java.lang.reflect.Constructor;

public class ViewsFactory {

    private static class InstanceHolder {
        private static final ViewsFactory INSTANCE = new ViewsFactory();
    }

    public static ViewsFactory getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public <C extends CustomView> C getCustomView(@NonNull Class<C> viewClass, Context context) {
        try {
            Constructor<?> ctor = viewClass.getConstructor(Context.class);
            return (C) ctor.newInstance(new Object[]{context});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
