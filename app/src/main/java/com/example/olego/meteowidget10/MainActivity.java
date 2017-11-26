package com.example.olego.meteowidget10;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    String tempOut = "0";
    String tempIn = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // startService(new Intent(this, MeteoService.class));
        GetAll();
    }
    public void GetAll () {
        APIInterface apiInterface;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<MultipleResource> call = apiInterface.doGetListResources();

        call.enqueue(new Callback<MultipleResource>() {
            @Override
            public void onResponse(Call<MultipleResource> call, Response<MultipleResource> response) {
                MultipleResource resource = response.body();
                tempOut = resource.outside;
                tempIn = resource.inside;
                int a = Integer.parseInt(tempOut);
                if (a > 65500) {
                    a = 65536 - a;
                    tempOut = "-" + String.valueOf(a);
                }
                Log.d("TAG", "Данные из метода GetAll: " + tempOut + " " + tempIn);
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText("Температура дома: " + tempIn + "\nТемпература на улице: " + tempOut);
            }

            @Override
            public void onFailure(Call<MultipleResource> call, Throwable t) {
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText("Не удалось загрузить данные");
                GetAll();
            }
        });

        Intent intent = new Intent(this, NewAppWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    public void ButtonClick (View view) {

        //startService(new Intent(this, MeteoService.class));

        Intent intent = new Intent(this, NewAppWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);


        /*
        finish();
        System.exit(0);*/
    }
}
