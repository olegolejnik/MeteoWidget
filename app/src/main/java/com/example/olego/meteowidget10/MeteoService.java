package com.example.olego.meteowidget10;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeteoService extends Service {

    public MeteoService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG", "Запустился onCreate в MeteoService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG", "Запустился onCreate в onStartCommand" + intent+ " "+flags+" "+startId);

        Timer myTimer = new Timer(); // Создаем таймер
        final Handler uiHandler = new Handler();
        //final TextView txtResult = (TextView)findViewById(R.id.txtResult);
        myTimer.schedule(new TimerTask() { // Определяем задачу
            @Override
            public void run() {
                //final String result = doLongAndComplicatedTask();
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        NewAppWidget newAppWidget = new NewAppWidget();
                        newAppWidget.GetTempFromServer();
                    }
                });
            }
        }, 1000L, 60L * 100); // интервал - 60000 миллисекунд, 0 миллисекунд до первого запуска.

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d("TAG", "Запустился onBind в MeteoService");
        NewAppWidget newAppWidget = new NewAppWidget();
        newAppWidget.GetTempFromServer();
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy() {
        Log.d("TAG", "Запустился onDestroy в MeteoService");
        super.onDestroy();
    }

}
