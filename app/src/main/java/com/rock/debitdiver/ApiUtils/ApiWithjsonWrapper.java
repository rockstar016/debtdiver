package com.rock.debitdiver.ApiUtils;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.rock.debitdiver.Utils.ShowProgressDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiWithjsonWrapper extends AsyncTask {
    private OkHttpClient okHttpClient;
    private String url, titleDialog;
    private ContentValues headerValues;
    private ContentValues values;
    private AsyncTaskCallback callback;
    private Context context;
    private boolean showDialog;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public ApiWithjsonWrapper(Context context, boolean showDialog, String titleDialog, String url, ContentValues headerValues, ContentValues values, AsyncTaskCallback callback){
        this.context = context;
        this.url = url;
        this.titleDialog = titleDialog;
        this.values = values;
        this.callback = callback;
        this.headerValues = headerValues;
        this.showDialog = showDialog;
        okHttpClient = GetMyOkhttpClient(headerValues);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(showDialog)
        ShowProgressDialog.showProgressDialog(context, titleDialog);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (showDialog)
            ShowProgressDialog.hideProgressDialog();
        callback.onResultService(o);
    }

    public JSONObject MakeJsonFromContentValues(ContentValues values)
    {
        try
        {
            JSONObject retVal = new JSONObject();
            for(Map.Entry<String, Object> key: values.valueSet()){
                retVal.put(key.getKey(), key.getValue().toString());
            }
            return retVal;
        }
        catch (Exception e)
        {
            return null;
        }

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            Response response;
            JSONObject bodyJson = MakeJsonFromContentValues(values);
            if(bodyJson == null)
            {
                return null;
            }

            RequestBody requestbody = RequestBody.create(JSON, bodyJson.toString());
            Request.Builder requestBuilder = new Request.Builder();
            Request request =   requestBuilder
                                .url(url)
                                .post(requestbody)
                                .build();
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static OkHttpClient GetMyOkhttpClient(final ContentValues headerValues)
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder newRequestBuilder  = request.newBuilder();
                if(headerValues.size() > 0)
                {
                    for(Map.Entry<String, Object> key: headerValues.valueSet()){
                        newRequestBuilder.addHeader(key.getKey(), key.getValue().toString());
                    }
                }
                Request newRequest = newRequestBuilder.build();
                return chain.proceed(newRequest);
            }
        });
        return builder.build();
    }

}
