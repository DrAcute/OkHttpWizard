package com.github.dracute.okhttp.wizard.demo;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.github.dracute.okhttp.wizard.demo.bean.BaseResult;
import com.github.dracute.okhttp.wizard.demo.bean.Been;
import com.github.dracute.okhttp.wizard.demo.http.DemoHttp;
import com.github.dracute.okhttp.wizard.lib.WizardConfig;
import com.github.dracute.okhttp.wizard.lib.WizardFactory;
import com.github.dracute.okhttp.wizard.lib.callback.WizardCallback;
import com.github.dracute.okhttp.wizard.lib.param.DownloadParam;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        WizardConfig config = WizardConfig.newBuilder().setHost("http://api.app.vraiai.com/index.php").setUseQuerySymbolInUrl(true).build();
        WizardFactory.initDefault(new OkHttpClient(), config);
        WizardFactory.getDefault().getService(DemoHttp.DemoInnerHttp.class).demo(1, 20, 32).enqueue(new WizardCallback<BaseResult<List<Been>>>() {
            @Override
            public void onSuccess(int code, BaseResult<List<Been>> listBaseResult) {
                Log.d("Tag", code + " === " + listBaseResult.getData().size());
            }

            @Override
            public void onFailure(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onProgress(long bytesRead, long contentLength, boolean done) {

            }
        });
        WizardFactory.getDefault().getService(DemoHttp.DemoInnerHttp.class).demo4(new DownloadParam(Environment.getExternalStorageDirectory().getAbsolutePath(), "temp", true)).enqueue(new WizardCallback<File>() {
            @Override
            public void onSuccess(int code, File file) {
                Log.d("Tag", code + " ");
            }

            @Override
            public void onFailure(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onProgress(long bytesRead, long contentLength, boolean done) {
                Log.d("TAG", bytesRead + " --- " + contentLength + " --- " + done);
            }
        });
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
