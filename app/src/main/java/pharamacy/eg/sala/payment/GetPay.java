//
//webView.setWebViewClient(new WebViewClient() {
//public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//        Toast.makeText(GetPay.this, "Processing webview url click...", Toast.LENGTH_LONG).show();
//
//        progressBar.setVisibility(View.VISIBLE);
//        if (url.startsWith("https://www.google.com/?accept=")) {
//        Toast.makeText(GetPay.this, "عملية مقبولة ", Toast.LENGTH_LONG).show();
//        finish();
//        return false;
//        } else if (url.startsWith("https://www.google.com/?fail=")) {
//
//        Toast.makeText(GetPay.this, "عملية غير مقبولة", Toast.LENGTH_LONG).show();
//        finish();
//        return false;
//        } else {
//        view.loadUrl(url);
//        webView.requestFocus(View.FOCUS_DOWN);
//        return true;
//        }
//
//
//        }
//
//public void onPageFinished(WebView view, String url) {
//
//        progressBar.setVisibility(View.GONE);
//
//        }
//
//public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//
//        progressBar.setVisibility(View.GONE);
//
//        }
//        });
//https://github.com/hwasiti/Android_Popup_Webview_handler_example/blob/master/app/src/main/java/com/example/haider/myapplication/MainActivity.java
package pharamacy.eg.sala.payment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import pharamacy.eg.sala.R;
import pharamacy.eg.sala.ReceyPro;

public class GetPay extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;
    private WebView mWebviewPop;
    private AlertDialog builder;
    private Toast mToast;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_pay);
        calling();
        handlling();
    }

    private void calling() {
        webView = findViewById(R.id.webView1);
        progressBar = findViewById(R.id.progressBar);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        webView.setWebViewClient(new UriWebViewClient());
        webView.setWebChromeClient(new UriChromeClient());

        mContext=this.getApplicationContext();

    }

    private void handlling() {
        openPay();
    }
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    public void openPay() {
        //            3f90876d-8936-4070-8159-00189b237ff6
            webView.loadUrl("https://app.vapulus.com/website/?siteId=6e2add7e-3697-4d6c-b6b3-e65e2b803027&amount=100&link=http:%2F%2Fsalaa.epizy.com&pageTitle=matrix%20mobile%20app&onaccept=http:%2F%2Fwww.google.com%2Fncr&onfail=http:%2F%2Fwww.msn.com%2F");

        }
    private class UriWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Toast.makeText(GetPay.this, "Processing webview url click...", Toast.LENGTH_LONG).show();

            progressBar.setVisibility(View.VISIBLE);
//            http:%2F%2Fwww.google.com%2Fncr&onfail=http:%2F%2Fwww.msn.com%2F
            if (url.startsWith("http://www.google.com")) {
                Toast.makeText(GetPay.this, "عملية مقبولة ", Toast.LENGTH_LONG).show();
                startActivity(new Intent(GetPay.this, ReceyPro.class).putExtra("accept","accept"));
                finish();
                return false;
            } else if (url.startsWith("http://www.msn.com")) {
                Toast.makeText(GetPay.this, "عملية غير مقبولة", Toast.LENGTH_LONG).show();
                startActivity(new Intent(GetPay.this, ReceyPro.class).putExtra("faild","faild"));
                finish();
                return false;
            } else {
                view.loadUrl(url);
                webView.requestFocus(View.FOCUS_DOWN);
                return true;
            }
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            Log.d("onReceivedSslError", "onReceivedSslError");
            //super.onReceivedSslError(view, handler, error);
        }
    }

    class UriChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            mWebviewPop = new WebView(mContext);
            mWebviewPop.setVerticalScrollBarEnabled(false);
            mWebviewPop.setHorizontalScrollBarEnabled(false);
            mWebviewPop.setWebViewClient(new UriWebViewClient());
            mWebviewPop.setWebChromeClient(new UriChromeClient());
            mWebviewPop.getSettings().setJavaScriptEnabled(true);
            mWebviewPop.getSettings().setSavePassword(true);
            mWebviewPop.getSettings().setSaveFormData(true);
            //mWebviewPop.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            // create an AlertDialog.Builder
            //the below did not give me .dismiss() method . See : https://stackoverflow.com/questions/14853325/how-to-dismiss-alertdialog-in-android

//            AlertDialog.Builder builder;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
//            } else {
//                builder = new AlertDialog.Builder(MainActivity.this);
//            }

            // set the WebView as the AlertDialog.Builder’s view

            builder = new AlertDialog.Builder(GetPay.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();


            builder.setTitle("");
            builder.setView(mWebviewPop);

            builder.setButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    mWebviewPop.destroy();
                    dialog.dismiss();


                }
            });


            builder.show();
            builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPop);
            resultMsg.sendToTarget();

            return true;
        }


        @Override
        public void onCloseWindow(WebView window) {

            //Toast.makeText(mContext,"onCloseWindow called",Toast.LENGTH_SHORT).show();


            try {
                mWebviewPop.destroy();
            } catch (Exception e) {

            }

            try {
                builder.dismiss();

            } catch (Exception e) {

            }


        }
    }
}


