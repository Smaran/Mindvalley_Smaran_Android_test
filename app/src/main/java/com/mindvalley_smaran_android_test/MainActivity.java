package com.mindvalley_smaran_android_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mindvalley_smaran_android_test.adapter.ImageAdapter;
import com.mindvalley_smaran_android_test.model.User;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ProgressDialog __progressDialog;
    User _userObject;
    private static ArrayList<User> _userArrrayList = new ArrayList<User>();
    protected AbsListView __listView;
    private RelativeLayout __helpview,__helpview2;
    private Button __proceed;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    MaterialRefreshLayout __materialRefreshLayout;
    FloatingActionButton __floatingActionButton;
    private SharedPreferences shared_preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initImageLoader(this);//This is required by the library to init the ImageLoader

        initViewControllers();// Initialising the view  controllers
        initTouchControllers();// Handling the click on the view items

        checkPref();//Check the preffresnce to enable tshe help view

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.item_clear_memory_cache:
                ImageLoader.getInstance().clearMemoryCache();
                return true;
            case R.id.item_clear_disc_cache:
                ImageLoader.getInstance().clearDiskCache();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mindvalley_smaran_android_test/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mindvalley_smaran_android_test/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            __progressDialog = new ProgressDialog(MainActivity.this);
            __progressDialog.setMessage("Please wait");
            __progressDialog.setCancelable(false);
            __progressDialog.show();
        }

        protected String doInBackground(String... params) {

            _userArrrayList = new ArrayList<User>();
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                    Log.d("Response: ", "> " + line);//response

                }

                try {
                    JSONArray jsonArray = new JSONArray(buffer.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        _userObject = new User();
                        JSONObject jObject = jsonArray.getJSONObject(i);

                        JSONObject image = jObject.getJSONObject("urls");
                        _userObject.setUrl_raw(image.getString("raw"));
                        _userObject.setUrl_full(image.getString("full"));
                        _userObject.setUrl_reguar(image.getString("regular"));
                        _userArrrayList.add(_userObject);

                    }


                } catch (JSONException e) {
                    Log.e("", e.toString());   //JSon Exception
                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (__progressDialog.isShowing()) {
                __progressDialog.dismiss();
            }
            __materialRefreshLayout.finishRefresh();

            loadItems(_userArrrayList);// Load the items on to the gridview

        }
    }


    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);

        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }


    private void loadItems(ArrayList<User> userArraylist) {

        ArrayList<String> items = new ArrayList<String>();

        ((GridView) __listView).setAdapter(null);
        ((GridView) __listView).setAdapter(new ImageAdapter(MainActivity.this, _userArrrayList));
    }

    private void initViewControllers() {
        __listView = (GridView) findViewById(R.id.grid);
        __floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        __helpview = (RelativeLayout) findViewById(R.id.helpView);
        __helpview2 = (RelativeLayout) findViewById(R.id.helpView2);
        __materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);

        shared_preference = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String mFirstTiem = shared_preference.getString("FIRST_TIME_HELP",
                "null");

        if (!(mFirstTiem.equalsIgnoreCase("null"))) {
            __floatingActionButton.setImageResource(android.R.drawable.ic_dialog_email);
            new JsonTask().execute("http://pastebin.com/raw/wgkJgazE");
        }

    }

    private void initTouchControllers() {
        __materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                //refreshing...
                new JsonTask().execute("http://pastebin.com/raw/wgkJgazE");
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //load more refreshing...
            }
        });


        assert __floatingActionButton != null;
        __floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shared_preference = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String mFirstTiem = shared_preference.getString("FIRST_TIME_HELP",
                        "null");
                String mSecondPage = shared_preference.getString("SECOND_PAGE",
                        "null");

                if (mFirstTiem.equalsIgnoreCase("null")) {
                    __floatingActionButton.setImageResource(android.R.drawable.ic_secure);
                    if(mSecondPage.equalsIgnoreCase("null")){
                        __helpview.setVisibility(View.GONE);
                        __helpview2.setVisibility(View.VISIBLE);
                        SharedPreferences.Editor editor =
                                shared_preference.edit();
                        editor.putString("SECOND_PAGE", "DONE");
                        editor.commit();

                    }else{
                        __helpview.setVisibility(View.GONE);
                        __helpview2.setVisibility(View.GONE);
                        SharedPreferences.Editor editor =
                                shared_preference.edit();
                        editor.putString("FIRST_TIME_HELP", "DONE");
                        editor.commit();

                        new JsonTask().execute("http://pastebin.com/raw/wgkJgazE");//Start the loading

                        __floatingActionButton.setImageResource(android.R.drawable.ic_dialog_email);
                    }
                }else{
                    __floatingActionButton.setImageResource(android.R.drawable.ic_dialog_email);
                    sendEmail();
                }

            }
        });


    }

    protected void sendEmail() {
        Log.i("Send email using", "");
        String[] TO = {"smaran.nss@gmail.com"};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reply to Test");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    void checkPref() {

        shared_preference = PreferenceManager.getDefaultSharedPreferences(this);
        String mFirstTiem = shared_preference.getString("FIRST_TIME_HELP",
                "null");
        if (mFirstTiem.equalsIgnoreCase("null")) {

            __helpview.setVisibility(View.VISIBLE);

        } else

        {
            __helpview.setVisibility(View.GONE);

        }
    }


}


