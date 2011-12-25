package com.nhn.socomlab.webapp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebApp04Activity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webapp01);

		WebView wv = (WebView)findViewById(R.id.webview);

		wv.loadUrl("http://testtomcat.cafe24.com");

		wv.setWebViewClient(new WebAppClient());

		wv.addJavascriptInterface(new WebAppInterface(), "nhnApp");

		WebSettings ws = wv.getSettings();
		ws.setJavaScriptEnabled(true);
	}

	private class WebAppInterface {
		public boolean hello(String msg) {
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			return true;
		}
	}

	private class WebAppClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}