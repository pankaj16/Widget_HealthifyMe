package com.pankaj.healthifymewidget.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;

public class ReadWriteJsonFileUtils {
	Context context;

	public ReadWriteJsonFileUtils(Context context) {
		this.context = context;
	}
	
	public void createJsonFileData(String filename, String mJsonResponse) {
	    try {
	        FileWriter file = new FileWriter("/data/data/" + context.getPackageName() + "/" + filename);
	        file.write(mJsonResponse);
	        file.flush();
	        file.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public String readJsonFileData(String filename) {
	    try {
	        File f = new File("/data/data/" + context.getPackageName() + "/" + filename);
			if(!f.exists()){
				return null;
			}
	        FileInputStream is = new FileInputStream(f);
	        int size = is.available();
	        byte[] buffer = new byte[size];
	        is.read(buffer);
	        is.close();
	        return new String(buffer);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
