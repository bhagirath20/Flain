package com.example.flainindia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private WebView webview;
    private AdView mAdView;

    //ad
    private InterstitialAd mInterstitialAd;
    //ad code finish

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //banner ad
        MobileAds.initialize(this, "ca-app-pub-6347747486396065~2989253167");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //ad finish

        webview=(WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new mywebClient());
        webview.loadUrl("https://www.theflain.com/");
        WebSettings webSettings=webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //other code try to solve whatsapp error
        // setwebviewclient  (new WebViewClient to  mywebClient )
        //other code end

        //zoom functionality
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        //zoom end

        //Google Interstitial Ads
        prepareAd();

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
         Log.i("hello", "world");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG"," Interstitial not loaded");
                        }

                        prepareAd();


                    }
                });

            }
        }, 10, 200, TimeUnit.SECONDS);
        //Google Interstitial Ads finish


    }
    public class mywebClient extends WebViewClient{
        //@Override
        //public void onPageStarted(WebView view, String url, Bitmap favicon){
            //super.onPageStarted(view,url,favicon);
        //}

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith("tel:") || url.startsWith("whatsapp:") || url.startsWith("sms:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            }
            return false;
        }
    }
    @Override
    public void onBackPressed(){
        if(webview.canGoBack()) {
            webview.goBack();
        }
        else{
            super.onBackPressed();
        }
    }
    //Google Interstitial Ads
    public void  prepareAd(){

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6347747486396065/8569964473");
        //ca-app-pub-6347747486396065/8569964473 original
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }
    //Google Interstitial Ads finish
}