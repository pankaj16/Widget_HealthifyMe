package com.pankaj.healthifymewidget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pankaj.healthifymewidget.Utils.Constants;
import com.pankaj.healthifymewidget.Utils.ReadWriteJsonFileUtils;
import com.pankaj.healthifymewidget.entities.Event;
import com.pankaj.healthifymewidget.webservice.RestWebService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);


        String data = new ReadWriteJsonFileUtils(HomeActivity.this).readJsonFileData(Constants.PARAMETER_EVENT_LIST);

        if (data == null) {
            new RestWebService(HomeActivity.this) {
                @Override
                public void onSuccess(String data) {
                    Type type = new TypeToken<Event>(){}.getType();
                    Event objEvent = new Gson().fromJson(data, type);
//                    Log.d("size", objEvent.getResponse().length+"");

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
                    new ReadWriteJsonFileUtils(HomeActivity.this).createJsonFileData(Constants.PARAMETER_EVENT_LIST, eventData);
                }
            }.serviceCall(Constants.API_GET_EVENT_LIST, "", true);
        }
    }
}
