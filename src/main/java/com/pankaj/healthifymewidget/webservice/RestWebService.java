package com.pankaj.healthifymewidget.webservice;

import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.pankaj.healthifymewidget.HealthifyMeApplication;
import com.pankaj.healthifymewidget.R;
import com.pankaj.healthifymewidget.Utils.Constants;
import com.pankaj.healthifymewidget.Utils.TransparentProgressDialog;

import android.content.Context;
import android.util.Log;

public class RestWebService {

	Context context;
	
	public RestWebService(Context context) {
		baseURL = Constants.BASE_URL;
		urlSuffix = Constants.SUFFIX_URL;
		this.context = context;
		transparentProgressDialog = new TransparentProgressDialog(context,
				R.drawable.needle);
	}
	
	String baseURL;
	String urlSuffix;
	TransparentProgressDialog transparentProgressDialog;


	private String getServiceURL(String resourceName, String extraParameters) {
		return baseURL + urlSuffix + resourceName + extraParameters;
	}

	public void onComplete() {
		if (transparentProgressDialog.isShowing()) {
			transparentProgressDialog.dismiss();
		}
	}


	public void onSuccess(String data) {

	}
	
	public void onError(VolleyError error){
		
	}


	public void serviceCall(String resourceName, String extraParameters, boolean showLoading) {
		String url = getServiceURL(resourceName, extraParameters);
		if(showLoading){
			if(!transparentProgressDialog.isShowing()){
				transparentProgressDialog.show();
			}
		}

		if (resourceName.equalsIgnoreCase(Constants.API_GET_EVENT_LIST)) {
			getCall(url);
		}

	}

	private void getCall(String url) {
		StringRequest req = new StringRequest(Method.GET, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String data) {
				onComplete();
				onSuccess(data);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				onComplete();
				Log.d("Error", error.toString());
				onError(error);
			}

		});
		req.setRetryPolicy(new RetryPolicy() {
			@Override
			public int getCurrentTimeout() {
				return Constants.TIMEOUT;
			}

			@Override
			public int getCurrentRetryCount() {
				return Constants.TIMEOUT;
			}

			@Override
			public void retry(VolleyError error) throws VolleyError {

			}
		});
		
		// add the request object to the queue to be executed
		HealthifyMeApplication.getInstance().getRequestQueue().add(req);
	}

}