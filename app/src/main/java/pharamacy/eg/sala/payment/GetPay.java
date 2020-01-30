package pharamacy.eg.sala.payment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import pharamacy.eg.sala.R;

public class GetPay extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_pay);
        calling();
        handlling();
        setTitle("أضف بيانات بطاقتك ");
    }

    private void calling() {
        webView = findViewById(R.id.webView1);
        progressBar = findViewById(R.id.progressBar);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handlling() {
        openPay();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    public void openPay() {

        //check if premission in manifest
        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        } else {
            webView.getSettings().setJavaScriptEnabled(true);

            webView.loadUrl("https://app.vapulus.com/website/?siteId=3f90876d-8936-4070-8159-00189b237ff6&amount=100&link=https:%2F%2Fstorage.googleapis.com%2Fvapulus-website%2Fgoogle-storage-cart.html&pageTitle=test%20web%20site&onaccept=http:%2F%2Fwww.google.com%2Fncr&onfail=http:%2F%2Fwww.msn.com%2F");
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    Toast.makeText(GetPay.this, "Processing webview url click...", Toast.LENGTH_LONG).show();

                    progressBar.setVisibility(View.VISIBLE);


                    if (url.startsWith("https://www.google.com/?accept=")) {
                        Toast.makeText(GetPay.this, "عملية مقبولة ", Toast.LENGTH_LONG).show();
                        finish();
                        return false;
                    } else if (url.startsWith("https://www.google.com/?fail=")) {

                        Toast.makeText(GetPay.this, "عملية غير مقبولة", Toast.LENGTH_LONG).show();
                        finish();
                        return false;
                    } else {
                        view.loadUrl(url);
                        webView.requestFocus(View.FOCUS_DOWN);
                        return true;
                    }


                }

                public void onPageFinished(WebView view, String url) {

                    progressBar.setVisibility(View.GONE);

                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                    progressBar.setVisibility(View.GONE);

                }
            });
        }

    }

    //check permission
    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
    }
}
