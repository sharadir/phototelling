
package com.phototell;

import android.app.Application;
import android.content.Context;

/**

 Application for tobe able to getApplicationContext outside of fragment/activity

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

