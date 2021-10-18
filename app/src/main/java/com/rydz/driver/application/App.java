package com.rydz.driver.application;

import android.app.Application;
import android.content.Context;
import com.rydz.driver.di.AppComponent;
import com.rydz.driver.di.AppModule;
import com.rydz.driver.di.DaggerAppComponent;
import com.rydz.driver.di.UtilsModule;
import com.rydz.driver.socket.AppSocketListener;

/**
 * Created by Sudesh on 05-02-2018.
 */

public class App extends Application {
    public AppComponent appComponent;
    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).utilsModule(new UtilsModule()).build();
        initializeSocket(getApplicationContext());

    }

    public void initializeSocket(Context applicationContext) {
        try
        {
            AppSocketListener.getInstance().initialize(applicationContext);
        }catch (Exception ex)
       {
           ex.printStackTrace();
       }

    }

    public void destroySocketListener() {
        try
        {
            AppSocketListener.getInstance().destroy();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public Context getContext()
    {
        return context;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        destroySocketListener();
    }
}
