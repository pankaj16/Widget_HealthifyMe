package com.pankaj.healthifymewidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pankaj.healthifymewidget.Utils.Constants;
import com.pankaj.healthifymewidget.Utils.DevicePreferences;
import com.pankaj.healthifymewidget.Utils.ReadWriteJsonFileUtils;
import com.pankaj.healthifymewidget.entities.Event;
import com.pankaj.healthifymewidget.webservice.RestWebService;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Pankaj on 19/03/16.
 */
public class HealthifyMeWidget extends AppWidgetProvider {

    //ACTIONS
    public static String ACTION_WIDGET_LINK = "ActionLink";
    public static String ACTION_WIDGET_NEXT_EVENT = "ActionNextEvent";
    public static String ACTION_WIDGET_PREVIOUS_EVENT = "ActionPreviousEvent";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        Log.d("Onupdate", "update");
        final int count = appWidgetIds.length;
        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            Intent active = new Intent(context, HealthifyMeWidget.class);
            active.setAction(ACTION_WIDGET_LINK);
            PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, PendingIntent.FLAG_CANCEL_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.textview_widget_link, actionPendingIntent);

            active = new Intent(context, HealthifyMeWidget.class);
            active.setAction(ACTION_WIDGET_NEXT_EVENT);
            actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
            remoteViews.setOnClickPendingIntent(R.id.layout_widget_next, actionPendingIntent);

            active = new Intent(context, HealthifyMeWidget.class);
            active.setAction(ACTION_WIDGET_PREVIOUS_EVENT);
            actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
            remoteViews.setOnClickPendingIntent(R.id.layout_widget_previous, actionPendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        boolean isAlaram = intent.getBooleanExtra("isAlarm", false);

        if(isAlaram){
            fetchData(context);
        } else if (intent.getAction().equals(ACTION_WIDGET_NEXT_EVENT)) {
            int position = new DevicePreferences().getInt(context, Constants.PREF_EVENT_POSITION, -1);
            if(position == -1){
                position = 0;
            } else{
                position++;
            }

            setViews(context, position);
        } else if (intent.getAction().equals(ACTION_WIDGET_PREVIOUS_EVENT)) {
            int position = new DevicePreferences().getInt(context, Constants.PREF_EVENT_POSITION, -1);
            if(position == -1){
                position = 0;
            } else if (position > 0){
                position--;
            }
            setViews(context, position);
        } else if (intent.getAction().equals(ACTION_WIDGET_LINK)) {
            try {
                int position = new DevicePreferences().getInt(context, Constants.PREF_EVENT_POSITION, -1);
                if(position == -1){
                    position = 0;
                }
                Event.ResponseList obj = getEvents(context, position);
                if(obj != null) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(obj.getUrl()));
                    webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(webIntent);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            Log.d("startuptest", "StartUpBootReceiver BOOT_COMPLETED");
            onEnabled(context);
        } else{
            super.onReceive(context, intent);
        }
    }

    private void fetchData(final Context context){
        new RestWebService(context) {
            @Override
            public void onSuccess(String data) {
                Type type = new TypeToken<Event>(){}.getType();
                Event objEvent = new Gson().fromJson(data, type);

                Map<Integer, Event.ResponseList> hashMap = new HashMap<Integer, Event.ResponseList>();
                if(objEvent != null && objEvent.getResponse() != null) {
                    int index = 0;
                    for (Event.ResponseList obj :
                            objEvent.getResponse()) {
                        if (obj.getStatus().equalsIgnoreCase("ONGOING") && obj.getCollege().equalsIgnoreCase("false")) {
                            if (obj.getThumbnail() != null) {
                                hashMap.put(index, obj);
                                index++;
                            }
                        }
                    }
                }
                Type eventType = new TypeToken<Map<Integer, Event.ResponseList>>(){}.getType();
                String eventData = new Gson().toJson(hashMap,eventType);
                new ReadWriteJsonFileUtils(context).createJsonFileData(Constants.PARAMETER_EVENT_LIST, eventData);
            }
        }.serviceCall(Constants.API_GET_EVENT_LIST, "", false);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
//        Log.d("Opions","changed");
    }

    @Override
    public void onEnabled(final Context context) {
        super.onEnabled(context);
        /***
         onEnabled is called if
         onAppWidgetOptionsChanged() method is also defined
         */
//        Log.d("OnEnabled", "Enabled");

        Intent myIntent = new Intent(context, HealthifyMeWidget.class);
        myIntent.putExtra("isAlarm", true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, myIntent,0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), 60*1000, pendingIntent);


        int position = new DevicePreferences().getInt(context, Constants.PREF_EVENT_POSITION, -1);
        if(position == -1){
            position = 0;
        }
        setViews(context, position);

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

//        Log.d("Disabled", "called");

        Intent intent = new Intent(context, HealthifyMeWidget.class);
        //use same pendingIntent which is used in setRepeating with same RequestCode/Id
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        alarmManager.cancel(sender);
    }


    private Event.ResponseList getEvents(Context context, int position){

        String data = new ReadWriteJsonFileUtils(context).readJsonFileData(Constants.PARAMETER_EVENT_LIST);
        Type eventType = new TypeToken<Map<Integer, Event.ResponseList>>(){}.getType();

        Map<Integer, Event.ResponseList> hashMap = new Gson().fromJson(data, eventType);

        Set entries = hashMap.entrySet();
        Iterator entryIter = entries.iterator();
        while (entryIter.hasNext()) {
            Map.Entry entry = (Map.Entry)entryIter.next();
            int key = (Integer)entry.getKey();  // Get the key from the entry.
            if(position == key) {
                Event.ResponseList value = (Event.ResponseList) entry.getValue();  // Get the value.
                return value;
            }
        }
        return null;
    }

    private void setViews(final Context context, final int position){
        final RemoteViews remoteViews;
        remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);
        final AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);

        final Event.ResponseList obj = getEvents(context, position);

        if (obj != null) {
            //Add new Event position
            modifyEventPosition(context, position);
            //Set RemoteView for widget
            remoteViews.setTextViewText(R.id.textview_widget_description, obj.getDescription());
            String openDate = getDateTime(obj.getStart_timestamp(),0);
            String closeDate = getDateTime(obj.getEnd_timestamp(),0);
            String startTime = getDateTime(obj.getStart_timestamp(),1);
            String endTime = getDateTime(obj.getEnd_timestamp(), 1);
            remoteViews.setTextViewText(R.id.textview_widget_openTime, openDate);
            remoteViews.setTextViewText(R.id.textview_widget_closeTime, closeDate);
            remoteViews.setTextViewText(R.id.textview_widget_challengeType, obj.getChallenge_type());
            String intervalString = String.format(context.getResources().getString(R.string.time_interval), startTime, endTime);
            remoteViews.setTextViewText(R.id.textview_widget_timeInterval, intervalString);
            remoteViews.setImageViewResource(R.id.imageview_widget, R.drawable.loading_300);
            appWidgetManager.updateAppWidget(new ComponentName(context,
                    HealthifyMeWidget.class), remoteViews);
            appWidgetManager.updateAppWidget(new ComponentName(context,
                    HealthifyMeWidget.class), remoteViews);

            final ImageView imageView = new ImageView(context);
            Picasso.with(context)
                    .load(obj.getThumbnail())
                    .placeholder(R.drawable.image_not_available)
                    .error(R.drawable.image_not_available)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Sucess", "Picasso");
                            remoteViews.setImageViewBitmap(R.id.imageview_widget, ((BitmapDrawable) imageView.getDrawable()).getBitmap());
                            appWidgetManager.updateAppWidget(new ComponentName(context,
                                    HealthifyMeWidget.class), remoteViews);
                        }

                        @Override
                        public void onError() {
                            Log.d("Error", "Picasso Error");
                            remoteViews.setImageViewResource(R.id.imageview_widget, R.drawable.image_not_available);
                            appWidgetManager.updateAppWidget(new ComponentName(context,
                                    HealthifyMeWidget.class), remoteViews);
                        }

                    });
        }
    }

    private void modifyEventPosition(Context context, int position){
        new DevicePreferences().addKey(context, Constants.PREF_EVENT_POSITION, position);
    }

    private String getDateTime(String dateTime, int position){
        return dateTime.split(",")[position];
    }
}
