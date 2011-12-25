package com.nhn.socomlab.webapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebAppScrollActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webapp01);

		WebView wv = (WebView)findViewById(R.id.webview);

		wv.loadUrl("http://testtomcat.cafe24.com");

		wv.setWebViewClient(new WebAppClient());

		wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	}

	private class WebAppClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}