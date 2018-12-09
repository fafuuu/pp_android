package com.app.fuchs.buchklub;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MyActivity extends Activity {

    WebView myWebView;
    TextView myResultView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //myResultView = (TextView) this.findViewById(R.id.myResult);

        myWebView = (WebView) this.findViewById(R.id.myWebView);
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl("https://buchklub.herokuapp.com/books/create");

        myWebView.addJavascriptInterface(new JavaScriptInterface(this, myWebView), "MyHandler");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        Button scan_btn = (Button) findViewById(R.id.button_scan);
        final Activity activity = this;

        scan_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                myWebView.loadUrl("https://buchklub.herokuapp.com/books/create");
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.initiateScan();
            }
        });

    }

    public void changeText(String someText) {
        Log.v("mylog", "changeText is called");
        myWebView.loadUrl("javascript:document.getElementById('test1').value =" + someText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {

            if(result.getContents() == null ) {

                Toast.makeText(this, "You canceled scannning", Toast.LENGTH_LONG).show();
            }
            else {

                Toast.makeText(this, "ISBN: " + result.getContents(), Toast.LENGTH_LONG).show();

                changeText(result.getContents());
            }
        }

        else {

            super.onActivityResult(requestCode, resultCode, data);

        }

    }

}

