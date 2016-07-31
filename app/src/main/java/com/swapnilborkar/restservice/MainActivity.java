package com.swapnilborkar.restservice;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.swapnilborkar.restservice.data.User;
import com.swapnilborkar.restservice.webservices.WebServiceTask;
import com.swapnilborkar.restservice.webservices.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private UserInfoTask mUserInfoTask = null;
    private UserEditTask mUserEditTask = null;
    private UserResetTask mUserResetTask = null;
    private UserDeleteTask mUserDeleteTask = null;
    private EditText mEmailText;
    private EditText mPasswordText;
    private EditText mNameText;
    private EditText mNoteText;
    private EditText mPhoneNumberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        showProgress(true);
        mUserInfoTask = new UserInfoTask();
        mUserInfoTask.execute();
    }

    private void initViews() {
        mEmailText = (EditText) findViewById(R.id.email);
        mPasswordText = (EditText) findViewById(R.id.password);
        mNameText = (EditText) findViewById(R.id.name);
        mPhoneNumberText = (EditText) findViewById(R.id.phoneNumber);
        mNoteText = (EditText) findViewById(R.id.note);
    }

    private void showProgress(boolean isShow) {
        findViewById(R.id.progress).setVisibility(isShow ? View.VISIBLE : View.GONE);
        findViewById(R.id.info_form).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void populateText() {
        User user = RESTServiceApplication.getInstance().getUser();
        mEmailText.setText(user.getEmail());
        mPasswordText.setText(user.getPassword());
        mNameText.setText(user.getName() == null ? "" : user.getName());
        mPhoneNumberText.setText(user.getPhoneNumber() == null ? "" : user.getPhoneNumber());
        mNoteText.setText(user.getNote() == null ? "" : user.getNote());
    }

    private abstract class ActivityWebServiceTask extends WebServiceTask {
        ActivityWebServiceTask(WebServiceTask webServiceTask) {
            super(MainActivity.this);
        }

        @Override
        public void showProgress() {
            MainActivity.this.showProgress(true);
        }

        @Override
        public void hideProgress() {
            MainActivity.this.showProgress(true);
        }

        @Override
        public void performSuccessfulOperation() {
            populateText();
        }
    }

    private class UserInfoTask extends ActivityWebServiceTask {
        public UserInfoTask() {
            super(mUserInfoTask);
        }

        @Override
        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            User user = RESTServiceApplication.getInstance().getUser();
            contentValues.put(Constants.ID, user.getId());
            contentValues.put(Constants.ACCESS_TOKEN, RESTServiceApplication.getInstance().getAccessToken());
            JSONObject jsonObject = WebServiceUtils
                    .requestJSONObject(Constants.INFO_URL, WebServiceUtils.METHOD.GET, contentValues, null);

            if (hasError(jsonObject)) {
                JSONArray jsonArray = jsonObject.optJSONArray(Constants.INFO);
                JSONObject jsonObject1 = jsonArray.optJSONObject(0);

                user.setName(jsonObject1.optString(Constants.NAME));
                if (user.getName().equalsIgnoreCase("null")) {
                    user.setName(null);
                }

                user.setPhoneNumber(jsonObject1.optString(Constants.PHONE_NUMBER));
                if (user.getPhoneNumber().equalsIgnoreCase("null")) {
                    user.setPhoneNumber(null);
                }

                user.setNote(jsonObject1.optString(Constants.NOTE));
                if (user.getNote().equalsIgnoreCase("null")) {
                    user.setNote(null);
                }

                user.setId(jsonObject1.optLong(Constants.ID_INFO));
                return true;
            }

            return false;
        }
    }

    private class UserEditTask extends ActivityWebServiceTask {
        public UserEditTask() {
            super(mUserEditTask);
        }

        @Override
        public boolean performRequest() {
            return true;
        }
    }

    private class UserResetTask extends ActivityWebServiceTask {
        public UserResetTask() {
            super(mUserResetTask);
        }

        @Override
        public boolean performRequest() {
            return true;
        }
    }

    private class UserDeleteTask extends ActivityWebServiceTask {
        public UserDeleteTask() {
            super(mUserDeleteTask);
        }

        @Override
        public boolean performRequest() {
            return true;
        }
    }

}
