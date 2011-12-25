package com.nhn.socomlab.webapp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebApp01Activity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webapp01);

		WebView wv = (WebView)findViewById(R.id.webview);

		wv.loadUrl("http://testtomcat.cafe24.com");
	}
}