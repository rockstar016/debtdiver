package com.rock.debitdiver.ApiUtils;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.rock.debitdiver.Utils.ShowProgressDialog;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiCallWrapper extends AsyncTask {
    private OkHttpClient okHttpClient;
    private String url, titleDialog;
    private ContentValues values, headerValues;
    private AsyncTaskCallback callback;
    private boolean showDialog;
    private Context context;

    public enum SERVICE_TYPE{
        POST,GET
    };
    private SERVICE_TYPE isPost;
    public ApiCallWrapper(Context context, SERVICE_TYPE isPost, boolean showDialog, String titleDialog, String url, ContentValues headerValues, ContentValues values, AsyncTaskCallback callback){
        this.context = context;
        this.url = url;
        this.titleDialog = titleDialog;
        this.showDialog = showDialog;
        this.values = values;
        this.callback = callback;
        this.isPost = isPost;
        this.headerValues = headerValues;
        okHttpClient = new OkHttpClient();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showDialog)
        ShowProgressDialog.showProgressDialog(context, titleDialog);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (showDialog)
            ShowProgressDialog.hideProgressDialog();
        callback.onResultService(o);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            Response response;
            if(isPost == SERVICE_TYPE.POST)
            {
                FormBody.Builder builder = new FormBody.Builder();
                for(Map.Entry<String, Object> key: values.valueSet()){
                    builder.add(key.getKey(), key.getValue().toString());
                }

                RequestBody requestbody = builder.build();

                Request.Builder requestBuilder = new Request.Builder();
                UpdateHeaderContent(requestBuilder);
                Request request = requestBuilder
                                                                    .url(url)
                                                                    .post(requestbody)
                                                                    .build();

                response = okHttpClient.newCall(request).execute();
            }
            else
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
                for(Map.Entry<String, Object> key: values.valueSet()){
                    urlBuilder.addQueryParameter(key.getKey(), key.getValue().toString());
                }
                String url = urlBuilder.build().toString();
                Request.Builder requestBuilder = new Request.Builder();
                UpdateHeaderContent(requestBuilder);
                Request request  = requestBuilder
                                                                                .url(url)
                                                                                .build();
                response = okHttpClient.newCall(request).execute();
            }
            if (response.isSuccessful()) {
                return response.body().string();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private  void UpdateHeaderContent(Request.Builder request)
    {
        if(headerValues.size() > 0)
        {
            for(Map.Entry<String, Object> key: values.valueSet()){
                request.addHeader(key.getKey(), key.getValue().toString());
            }
        }
    }
}
