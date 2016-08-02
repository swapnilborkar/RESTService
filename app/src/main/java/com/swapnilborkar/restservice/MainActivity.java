package com.swapnilborkar.restservice;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        findViewById(R.id.progress).setVisibility(isShow ? View.GONE : View.VISIBLE);
        findViewById(R.id.info_form).setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    private void populateText() {
        User user = RESTServiceApplication.getInstance().getUser();
        mEmailText.setText(user.getEmail());
        mPasswordText.setText(user.getPassword());
        mNameText.setText(user.getName() == null ? "" : user.getName());
        mPhoneNumberText.setText(user.getPhoneNumber() == null ? "" : user.getPhoneNumber());
        mNoteText.setText(user.getNote() == null ? "" : user.getNote());
    }

    public void clickUpdateButton(View view) {
        if (mPasswordText.getText().toString().trim().length() >= 5) {
            showProgress(true);
            mUserEditTask = new UserEditTask();
            mUserEditTask.execute();
        } else {
            Toast.makeText(this, R.string.error_password_length, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickDeleteButton(View view) {
        showConfigurationDialog(new ConfirmationListener() {
            @Override
            public void onConfirmation(boolean isConfirmed) {
                if (isConfirmed) {
                    showProgress(true);
                    mUserDeleteTask = new UserDeleteTask();
                    mUserDeleteTask.execute();
                }
            }
        });

    }

    public void clickResetButton(View view) {
        showConfigurationDialog(new ConfirmationListener() {
            @Override
            public void onConfirmation(boolean isConfirmed) {
                if (isConfirmed) {
                    showProgress(true);
                    mUserResetTask = new UserResetTask();
                    mUserResetTask.execute();
                }
            }
        });
    }

    public void clickSignOutButton(View view) {
        showLoginScreen();
    }

    private void showLoginScreen() {
        Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void showConfigurationDialog(final ConfirmationListener confirmationListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure? This action cannot be undone!");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirmationListener.onConfirmation(false);
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirmationListener.onConfirmation(true);
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

    }

    private interface ConfirmationListener {
        void onConfirmation(boolean isConfirmed);
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

            if (!hasError(jsonObject)) {
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

            ContentValues contentValues = new ContentValues();
            User user = RESTServiceApplication.getInstance().getUser();
            contentValues.put(Constants.ID, user.getId());
            contentValues.put(Constants.PASSWORD, mPasswordText.getText().toString());
            contentValues.put(Constants.PHONE_NUMBER, mPhoneNumberText.getText().toString());
            contentValues.put(Constants.NOTE, mNoteText.getText().toString());
            contentValues.put(Constants.NAME, mNameText.getText().toString());

            ContentValues urlValues = new ContentValues();
            urlValues.put(Constants.ACCESS_TOKEN, RESTServiceApplication.getInstance().getAccessToken());


            JSONObject jsonObject = WebServiceUtils.requestJSONObject(
                    Constants.UPDATE_URL, WebServiceUtils.METHOD.POST,
                    urlValues, contentValues);

            if (!hasError(jsonObject)) {
                JSONArray jsonArray = jsonObject.optJSONArray(Constants.INFO);
                JSONObject jsonObject1 = jsonArray.optJSONObject(0);
                user.setName(jsonObject1.optString(Constants.NAME));
                user.setPhoneNumber(jsonObject1.optString(Constants.PHONE_NUMBER));
                user.setNote(jsonObject1.optString(Constants.NOTE));
                user.setPassword(jsonObject1.optString(Constants.PASSWORD));
                return true;
            }
            return false;
        }
    }

    private class UserResetTask extends ActivityWebServiceTask {
        public UserResetTask() {
            super(mUserResetTask);
        }
        @Override
        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            User user = RESTServiceApplication.getInstance().getUser();
            contentValues.put(Constants.ID, user.getId());
            contentValues.put(Constants.ACCESS_TOKEN, RESTServiceApplication.getInstance().getAccessToken());

            JSONObject jsonObject = WebServiceUtils.requestJSONObject(Constants.RESET_URL,
                    WebServiceUtils.METHOD.POST, contentValues, null);

            if (!hasError(jsonObject)) {
                user.setName("");
                user.setPhoneNumber("");
                user.setNote("");
                return true;
            }
            return false;
        }
    }

    private class UserDeleteTask extends ActivityWebServiceTask {
        public UserDeleteTask() {
            super(mUserDeleteTask);
        }

        @Override
        public boolean performRequest() {
            ContentValues contentValues = new ContentValues();
            User user = RESTServiceApplication.getInstance().getUser();
            contentValues.put(Constants.ID, user.getId());
            contentValues.put(Constants.ACCESS_TOKEN, RESTServiceApplication.getInstance().getAccessToken());

            JSONObject jsonObject = WebServiceUtils.requestJSONObject(Constants.DELETE_URL, WebServiceUtils.METHOD.DELETE,
                    contentValues, null);

            if (!hasError(jsonObject)) {
                RESTServiceApplication.getInstance().setUser(null);
                return true;
            }
            return false;
        }

        @Override
        public void performSuccessfulOperation() {
            showLoginScreen();
        }
    }

}
