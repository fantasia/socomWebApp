package com.nhn.socomlab.webapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebAppCacheTest1Activity extends Activity {
	private static final String URL = "http://test.was.ntalk.naver.com/tmplTest";
	private WebView wv = null;
	private ProgressBar pb = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webapp06);

		wv = (WebView)findViewById(R.id.webview);
		pb = (ProgressBar)findViewById(R.id.progress);

		wv.loadUrl(this.URL);

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
}