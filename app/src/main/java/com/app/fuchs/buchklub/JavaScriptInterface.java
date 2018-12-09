package com.app.fuchs.buchklub;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class JavaScriptInterface {
    protected MyActivity parentActivity;
    protected WebView mWebView;


    public JavaScriptInterface(MyActivity _activity, WebView _webView)  {
        parentActivity = _activity;
        mWebView = _webView;

    }

    @JavascriptInterface
    public void loadURL(String url) {
        final String u = url;

        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(u);
            }
        });
    }
}