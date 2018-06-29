package com.ga.cloudfeed.view.activity;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ga.cloudfeed.CloudFeedApplication;
import com.ga.cloudfeed.R;
import com.ga.cloudfeed.viewmodel.ItemViewModel;

import javax.inject.Inject;

public class WebViewActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private String link;
    private String title;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ItemViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        //WebView.setWebContentsDebuggingEnabled(true);

        WebView webView = findViewById(R.id.wb_itemWebview);

        progressBar = findViewById(R.id.pb_webview);


        ((CloudFeedApplication) getApplication())
                .getAppComponent()
                .inject(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ItemViewModel.class);

        viewModel.setModelById(getIntent().getExtras().getString("modelLink"));


        startWebView(webView, viewModel);

    }

    private void updateItem(MenuItem item) {
        if (viewModel.getModel().readLater) item.setIcon(R.drawable.ic_bookmark_18pt_2x);
        else item.setIcon(R.drawable.ic_bookmark_border_black_24dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_webview_menu, menu);
        updateItem(menu.findItem(R.id.action_read_later));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_read_later:
                viewModel.setReadLater();
                viewModel.getModel().readLater = !viewModel.getModel().readLater;
                updateItem(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void startWebView(WebView webView, ItemViewModel viewModel) {
        String html = viewModel.getHtml();

        link = viewModel.getModel().link;
        title = viewModel.getModel().title;
        setTitle(title);
        JavaScriptInterface jsInterface = new JavaScriptInterface();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(jsInterface, "JSInterface");
        webView.setWebViewClient(new CustomWebViewClient());
        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(ProgressBar.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String
                failingUrl) {
            Toast.makeText(WebViewActivity.this, description, Toast.LENGTH_SHORT).show();
        }
    }

    public class JavaScriptInterface {
        @JavascriptInterface
        public void share() {
            ShareCompat.IntentBuilder.from(WebViewActivity.this)
                    .setType("text/plain")
                    .setChooserTitle(title)
                    .setText(link)
                    .startChooser();
        }

        @JavascriptInterface
        public void visit() {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
        }
    }
}
