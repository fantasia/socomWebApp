package com.nhn.socomlab.webapp;

import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class WebAppActivity extends Activity implements View.OnClickListener {

	private static final String WEINRE_FORMAT = "javascript:(function() { var s = document.createElement('script'); s.type = 'text/javascript';  s.async = true; s.src = 'http://%s:%s/target/target-script-min.js#anonymous';  var x = document.getElementsByTagName('script')[0]; x.parentNode.insertBefore(s, x); })();";
	private static final String JQUERY_DELAY_LOAD = "javascript:(function() { var s = document.createElement('script'); s.type = 'text/javascript';  s.async = true; s.src = 'http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.7.1.min.js';  var x = document.getElementsByTagName('script')[0]; x.parentNode.insertBefore(s, x); })();";

	private WebView wv = null;
	private ProgressBar pb = null;
	private EditText etUrl = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webapp);

		wv = (WebView)findViewById(R.id.webview);
		pb = (ProgressBar)findViewById(R.id.progress);
		etUrl = (EditText)findViewById(R.id.etUrl);
		etUrl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				loadUrlFromEdittext();
				return true;
			}
		});

		try {
			wv.loadUrl(getIntent().getStringExtra("url"));
		} catch (Exception e) {
		}

		wv.setWebViewClient(new WebAppClient());
		wv.setWebChromeClient(new WebAppChromeClient());

		wv.addJavascriptInterface(new WebAppInterface(), "nhnApp");
		wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

		WebSettings ws = wv.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setDatabaseEnabled(true);
		ws.setPluginsEnabled(true);
		ws.setAppCacheEnabled(true);
		ws.setGeolocationEnabled(true);
		ws.setDomStorageEnabled(true);
	}

	@Override
	public void onBackPressed() {
		if (wv.canGoBack()) {
			wv.goBack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnMove:
				loadUrlFromEdittext();
				break;

			default:
				break;
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		menu.add("WEINRE");
		menu.add("jQuery");
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (0 == "WEINRE".compareToIgnoreCase(item.getTitle().toString())) {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			View dialogView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_weinre, null);
			final TextView tvWeinreIp = (TextView)dialogView.findViewById(R.id.tvWeinreIp);
			final TextView tvWeinrePort = (TextView)dialogView.findViewById(R.id.tvWeinrePort);
			adb.setView(dialogView);
			adb.setTitle("WEINRE Setting");
			adb.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String weinreJs = String.format(WEINRE_FORMAT, tvWeinreIp.getText().toString(), tvWeinrePort.getText().toString());
					wv.loadUrl(weinreJs);
				}
			});
			adb.create().show();
		} else if (0 == "jQuery".compareToIgnoreCase(item.getTitle().toString())) {
			wv.loadUrl(JQUERY_DELAY_LOAD);
		}
		return super.onOptionsItemSelected(item);
	}

	private void loadUrlFromEdittext() {
		String url = etUrl.getText().toString();
		if (!url.toLowerCase().startsWith("http://")) {
			url = "http://" + url;
		}
		wv.loadUrl(url);

		// 키보드 숨김
		InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(etUrl.getWindowToken(), 0);
	}

	private class WebAppInterface {
	}

	private class WebAppChromeClient extends WebChromeClient {

		@Override
		public Bitmap getDefaultVideoPoster() {
			log(String.format("WebAppClient::getDefaultVideoPoster"));
			return super.getDefaultVideoPoster();
		}

		@Override
		public View getVideoLoadingProgressView() {
			log(String.format("WebAppClient::getVideoLoadingProgressView"));
			return super.getVideoLoadingProgressView();
		}

		@Override
		public void getVisitedHistory(ValueCallback<String[]> callback) {
			log(String.format("WebAppClient::getVisitedHistory"));
			super.getVisitedHistory(callback);
		}

		@Override
		public void onCloseWindow(WebView window) {
			log(String.format("WebAppClient::onCloseWindow"));
			super.onCloseWindow(window);
		}

		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			log(String.format("WebAppClient::onConsoleMessage - consoleMessage : %s", consoleMessage.message()));
			return super.onConsoleMessage(consoleMessage);
		}

		@Override
		public void onConsoleMessage(String message, int lineNumber, String sourceID) {
			log(String.format("WebAppClient::onConsoleMessage - message : %s, line : %d, sourceID : %s", message, lineNumber, sourceID));
			super.onConsoleMessage(message, lineNumber, sourceID);
		}

		@Override
		public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
			log(String.format("WebAppClient::onCreateWindow"));
			return super.onCreateWindow(view, dialog, userGesture, resultMsg);
		}

		@Override
		public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, QuotaUpdater quotaUpdater) {
			log(String.format("WebAppClient::onExceededDatabaseQuota"));
			super.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota, estimatedSize, totalUsedQuota, quotaUpdater);
		}

		@Override
		public void onGeolocationPermissionsHidePrompt() {
			log(String.format("WebAppClient::onGeolocationPermissionsHidePrompt"));
			super.onGeolocationPermissionsHidePrompt();
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
			log(String.format("WebAppClient::onGeolocationPermissionsShowPrompt"));
			super.onGeolocationPermissionsShowPrompt(origin, callback);
		}

		@Override
		public void onHideCustomView() {
			log(String.format("WebAppClient::onHideCustomView"));
			super.onHideCustomView();
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			log(String.format("WebAppClient::onJsAlert - url : %s, message : %s", url, message));
			return super.onJsAlert(view, url, message, result);
		}

		@Override
		public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
			log(String.format("WebAppClient::onJsBeforeUnload - url : %s, message : %s", url, message));
			return super.onJsBeforeUnload(view, url, message, result);
		}

		@Override
		public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
			log(String.format("WebAppClient::onJsConfirm - url : %s, message : %s", url, message));
			return super.onJsConfirm(view, url, message, result);
		}

		@Override
		public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
			log(String.format("WebAppClient::onJsPrompt - url : %s, message : %s", url, message));
			return super.onJsPrompt(view, url, message, defaultValue, result);
		}

		@Override
		public boolean onJsTimeout() {
			log(String.format("WebAppClient::onJsTimeout"));
			return super.onJsTimeout();
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			log(String.format("WebAppClient::onProgressChanged - newProgress : %d", newProgress));

			if (newProgress != 100) {
				pb.setVisibility(View.VISIBLE);
				pb.setProgress(newProgress);
			} else {
				pb.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, QuotaUpdater quotaUpdater) {
			log(String.format("WebAppClient::onReachedMaxAppCacheSize"));
			super.onReachedMaxAppCacheSize(spaceNeeded, totalUsedQuota, quotaUpdater);
		}

		@Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			log(String.format("WebAppClient::onReceivedIcon"));
			super.onReceivedIcon(view, icon);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			log(String.format("WebAppClient::onReceivedTitle - title : %s", title));
			setTitle(title);
			super.onReceivedTitle(view, title);
		}

		@Override
		public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
			log(String.format("WebAppClient::onReceivedTouchIconUrl - url : %s", url));
			super.onReceivedTouchIconUrl(view, url, precomposed);
		}

		@Override
		public void onRequestFocus(WebView view) {
			log(String.format("WebAppClient::onRequestFocus"));
			super.onRequestFocus(view);
		}

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			log(String.format("WebAppClient::onShowCustomView"));
			super.onShowCustomView(view, callback);
		}

	}

	private class WebAppClient extends WebViewClient {
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			log(String.format("WebAppClient::shouldInterceptRequest - url : %s", url));
			try {
				WebResourceResponse wrr = null;
				if (0 == url.compareToIgnoreCase("http://static.naver.net/www/u/2010/0611/nmms_215646753.gif")) {
					//wrr = new WebResourceResponse("image/png", "", getAssets().open("daum.png"));
					wrr = new WebResourceResponse("image/png", "", new URL("http://www.google.co.kr/images/srpr/logo3w.png").openStream());
				} else if (0 == url.compareToIgnoreCase("http://static.naver.com/header/h1/naver.gif")) {
					wrr = new WebResourceResponse("image/png", "", new URL("http://img-contents.daum-img.net/2010ci/daumlogo.gif").openStream());
				}
				return wrr;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return super.shouldInterceptRequest(view, url);
		}

		@Override
		public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
			log(String.format("WebAppClient::doUpdateVisitedHistory - url : %s", url));
			super.doUpdateVisitedHistory(view, url, isReload);
		}

		@Override
		public void onFormResubmission(WebView view, Message dontResend, Message resend) {
			log(String.format("WebAppClient::onFormResubmission"));
			super.onFormResubmission(view, dontResend, resend);
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			log(String.format("WebAppClient::onLoadResource - url : %s", url));
			super.onLoadResource(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			log(String.format("WebAppClient::onPageFinished - url : %s", url));
			etUrl.setText(url);
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			log(String.format("WebAppClient::onPageStarted - url : %s", url));
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			log(String.format("WebAppClient::onReceivedError"));
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
			log(String.format("WebAppClient::onReceivedHttpAuthRequest"));
			super.onReceivedHttpAuthRequest(view, handler, host, realm);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			log(String.format("WebAppClient::onReceivedSslError"));
			super.onReceivedSslError(view, handler, error);
		}

		@Override
		public void onScaleChanged(WebView view, float oldScale, float newScale) {
			log(String.format("WebAppClient::onScaleChanged - old : %f, new : %f", oldScale, newScale));
			super.onScaleChanged(view, oldScale, newScale);
		}

		@Override
		public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
			log(String.format("WebAppClient::onTooManyRedirects"));
			super.onTooManyRedirects(view, cancelMsg, continueMsg);
		}

		@Override
		public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
			log(String.format("WebAppClient::onUnhandledKeyEvent - event : %s", event.toString()));
			super.onUnhandledKeyEvent(view, event);
		}

		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			log(String.format("WebAppClient::shouldOverrideKeyEvent - event : %s", event.toString()));
			return super.shouldOverrideKeyEvent(view, event);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			log(String.format("WebAppClient::shouldOverrideUrlLoading - url : %s", url));
			view.loadUrl(url);
			return true;
		}
	}

	private void log(String msg) {
		Log.d("WebApp", msg);
	}

	private void alert(String msg) {
		Toast.makeText(this, msg, 0).show();
	}

}