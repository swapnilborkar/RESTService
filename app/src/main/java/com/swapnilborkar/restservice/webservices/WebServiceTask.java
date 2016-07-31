package com.swapnilborkar.restservice.webservices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.swapnilborkar.restservice.Constants;
import com.swapnilborkar.restservice.R;

import org.json.JSONObject;

/**
 * Created by SWAPNIL on 28-07-2016.
 */
public abstract class WebServiceTask extends AsyncTask<Void, Void, Boolean> {

    public static final String LOG_TAG = WebServiceTask.class.getName();
    private String mMessage;
    private Context mContext;

    public WebServiceTask(Context context) {
        this.mContext = context;
    }

    public abstract void showProgress();

    public abstract boolean performRequest();

    public abstract void performSuccessfulOperation();

    public abstract void hideProgress();

    @Override
    protected void onPreExecute() {
        showProgress();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (!WebServiceUtils.hasInternetConnection(mContext)) {
            mMessage = Constants.CONNECTION_MESSAGE;
            return false;
        }
        return performRequest();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        hideProgress();
        if (success) {
            performSuccessfulOperation();
        }

        if (mMessage != null && !mMessage.isEmpty()) {
            Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        hideProgress();
    }

    public boolean hasError(JSONObject obj) {
        if (obj != null) {
            int status = obj.optInt(Constants.STATUS);
            Log.d(LOG_TAG, "Response: " + obj.toString());
            mMessage = obj.optString(Constants.MESSAGE);

            return status == Constants.STATUS_ERROR || status == Constants.STATUS_UNAUTHORIZED;
        }

        mMessage = mContext.getString(R.string.error_url_not_found);
        return true;
    }
}

