package com.rock.debitdiver.ApiUtils;

import android.content.Context;
import android.os.AsyncTask;

import com.rock.debitdiver.Utils.ShowProgressDialog;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiWithTextWrapper extends AsyncTask {
    private OkHttpClient okHttpClient;
    private String url, titleDialog;
    private String values;
    private AsyncTaskCallback callback;
    private Context context;
    private boolean showDialog;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public ApiWithTextWrapper(Context context, boolean showDialog, String titleDialog, String url, String values, AsyncTaskCallback callback){
        this.context = context;
        this.url = url;
        this.titleDialog = titleDialog;
        this.showDialog = showDialog;
        this.values = values;
        this.callback = callback;
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
        try
        {
            RequestBody body = RequestBody.create(JSON, values);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
