package com.example.olego.meteowidget10;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static String tempOut = "--";
    static String tempIn = "--";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d("TAG", "Значения полученные в updateAppWidget: " + tempOut + " " + tempIn);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        DateFormat df = new SimpleDateFormat("HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        views.setTextViewText(R.id.appwidget_text, "На улице: "+ tempOut+" C°\n"+"Дома:       "+tempIn+" C°");
        views.setTextViewText(R.id.appwidget_time, ""+date);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
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
                        GetTempFromServer();
                        for (int appWidgetId : appWidgetIds) {
                            updateAppWidget(context, appWidgetManager, appWidgetId);
                        }

                    }
                });
            }
        }, 5000L); // интервал - 60000 миллисекунд, 0 миллисекунд до первого запуска.
        GetTempFromServer();
        Log.d("TAG", "Значения полученные в onUpdate: " + tempOut + " " + tempIn);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d("TAG", "Значения полученные в onEnabled: " + tempOut + " " + tempIn);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d("TAG", "Значения полученные в onDisabled: " + tempOut + " " + tempIn);
    }

    public void GetTempFromServer () {
        APIInterface apiInterface;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<MultipleResource> call = apiInterface.doGetListResources();

        call.enqueue(new Callback<MultipleResource>() {
            @Override
            public void onResponse(Call<MultipleResource> call, Response<MultipleResource> response) {
                MultipleResource resource = response.body();
                tempOut = resource.outside; tempIn = resource.inside;
                int a = Integer.parseInt(tempOut);
                if (a > 65500) {a = 65536 - a; tempOut = "-"+String.valueOf(a);}
                Log.d("TAG", "Значения полученные в GetTempFromServer: " + tempOut + " " + tempIn);
            }
            @Override
            public void onFailure(Call<MultipleResource> call, Throwable t) {
                //GetTempOnWidget();
            }
        });


    }
}


/*public class NewAppWidget extends AppWidgetProvider {
    static String tempOut = "--";
    static String tempIn = "--";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d("TAG","Вход в updateAppWidget");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
       // views.setTextViewText(R.id.appwidget_text, "На улице: "+ tempOut+" C°\n"+"Дома:       "+tempIn+" C°");
        views.setTextViewText(R.id.appwidget_text,"Привет");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d("TAG","Начало onUpdate, данные: "+tempOut+ " " +tempIn);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d("TAG","Начало onEnabled, данные: "+tempOut+ " " +tempIn);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d("TAG","Начало onDisabled, данные: "+tempOut+ " " +tempIn);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        GetTempFromServer();
        Log.d("TAG","Начало onReceive, данные: "+tempOut+ " " +tempIn);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        updateAppWidget(context, appWidgetManager,0);

    }

    public void GetTempFromServer () {
        APIInterface apiInterface;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<MultipleResource> call = apiInterface.doGetListResources();

        call.enqueue(new Callback<MultipleResource>() {
            @Override
            public void onResponse(Call<MultipleResource> call, Response<MultipleResource> response) {
                MultipleResource resource = response.body();
                tempOut = resource.outside; tempIn = resource.inside;
                int a = Integer.parseInt(tempOut);
                if (a > 65500) {a = 65536 - a; tempOut = "-"+String.valueOf(a);}
                Log.d("TAG", "Значения полученные в GetTempFromServer: " + tempOut + " " + tempIn);
            }
            @Override
            public void onFailure(Call<MultipleResource> call, Throwable t) {
                //GetTempOnWidget();
            }
        });
    }
}

   /* public static String tempOut = "";
    public static String tempIn = "";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, "На улице: "+ tempOut+" C°\n"+"Дома:       "+tempIn+" C°");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

        Log.d("TAG",tempOut+" Значение на входе в updateAppWidget");

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d("TAG", tempOut + " Значение на входе в onUpdate");
        GetTempFromServer();
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d("TAG",tempOut+" Значение на входе в onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d("TAG",tempOut+" Значение на входе в onDisabled");
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("TAG","Начало onReceive - "+tempOut);
        GetTempFromServer();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            updateAppWidget(context, appWidgetManager, 0);

    }

    public void GetTempFromServer () {
        APIInterface apiInterface;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<MultipleResource> call = apiInterface.doGetListResources();

        call.enqueue(new Callback<MultipleResource>() {
            @Override
            public void onResponse(Call<MultipleResource> call, Response<MultipleResource> response) {
                MultipleResource resource = response.body();
                tempOut = resource.outside; tempIn = resource.inside;
                int a = Integer.parseInt(tempOut);
                if (a > 65500) {a = 65536 - a; tempOut = "-"+String.valueOf(a);}
                Log.d("TAG", "Значения полученные в GetTempFromServer: " + tempOut + " " + tempIn);
            }
            @Override
            public void onFailure(Call<MultipleResource> call, Throwable t) {
                //GetTempOnWidget();
            }
        });
    }
}
*/