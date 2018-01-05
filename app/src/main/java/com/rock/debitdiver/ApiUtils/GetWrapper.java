package com.rock.debitdiver.ApiUtils;

import android.content.Context;
import android.os.AsyncTask;

import com.rock.debitdiver.Utils.ShowProgressDialog;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GetWrapper extends AsyncTask {
    private OkHttpClient okHttpClient;
    private String url;
    private AsyncTaskCallback callback;
    private Context context;
    private String dialogTitle;
    private boolean showDialog;
    public GetWrapper(Context context, boolean showDialog, String dialogTitle, String url, AsyncTaskCallback callback) {
        this.url = url;
        this.dialogTitle = dialogTitle;
        this.showDialog = showDialog;
        this.callback = callback;
        this.context = context;
        okHttpClient = new OkHttpClient();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(showDialog)
            ShowProgressDialog.showProgressDialog(context, dialogTitle);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try
        {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(showDialog)
            ShowProgressDialog.hideProgressDialog();
        callback.onResultService(o);
    }
}
