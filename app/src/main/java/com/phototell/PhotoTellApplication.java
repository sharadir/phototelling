
package com.phototell;

import android.app.Application;
import android.content.Context;

/**
 * Created by user on 22/03/2017.
 */

public class PhotoTellApplication extends Application {

        private static Application sApplication;

        public static Application getApplication() {
            return sApplication;
        }

        public static Context getContext() {
            return getApplication().getApplicationContext();
        }

        @Override
        public void onCreate() {
            super.onCreate();
            sApplication = this;
        }
}

