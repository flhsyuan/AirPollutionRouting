package airpollutionrouting.com.mapsexample;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AircastingActivity extends AppCompatActivity{

    private WebView webView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aircasting);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        webView1 = (WebView) findViewById(R.id.web_view);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.setWebViewClient(new WebViewClient());

        webView1.loadUrl(url);

        webView1.getSettings().setUseWideViewPort(true);
        webView1.getSettings().setLoadWithOverviewMode(true);
        webView1.getSettings().setBuiltInZoomControls(true);
    }
}
