package com.nhn.socomlab.webapp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

public final class HttpUtil {
	private static TrustManager[] trustManagers;

	public static class _FakeX509TrustManager implements X509TrustManager {
		private static final java.security.cert.X509Certificate[] _AcceptedIssuers = new java.security.cert.X509Certificate[] {};

		public void checkClientTrusted(X509Certificate[] arg0, String arg1) 
			throws CertificateException { }
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) 
			throws CertificateException { }
		public boolean isClientTrusted(X509Certificate[] chain) {
			return true;
		}
		public boolean isServerTrusted(X509Certificate[] chain) {
			return true;
		}
		@Override
		public void checkClientTrusted( java.security.cert.X509Certificate[] chain, String authType)
			throws java.security.cert.CertificateException {}
		@Override
		public void checkServerTrusted( java.security.cert.X509Certificate[] chain, String authType)
			throws java.security.cert.CertificateException {}
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return _AcceptedIssuers;
		}
	}

	public static void allowAllSSL() {
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		javax.net.ssl.SSLContext context = null;
		if (trustManagers == null) {
			trustManagers = new javax.net.ssl.TrustManager[] { new _FakeX509TrustManager() };
		}

		try {
			context = javax.net.ssl.SSLContext.getInstance("TLS");
			context.init(null, trustManagers, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
	}

	public synchronized String GetHTTPContents(String url, String reqBody, String encoding) throws InterruptedException, IOException {
		String resBody = "";
		URL u;
		OutputStream out = null;
		InputStream in = null;
		HttpURLConnection conn = null;

		u = new URL(url);

		if (0 == url.substring(0, 8).compareToIgnoreCase("https://")) {
			// HTTPS 신뢰할수 없는 사이트 무시 코드
			allowAllSSL();
			conn = (HttpsURLConnection) u.openConnection();
		} else {
			conn = (HttpURLConnection) u.openConnection();
		}

		if( reqBody != null && reqBody.length() > 0 ) {
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();

			out = conn.getOutputStream();
			byte[] bytes = reqBody.getBytes(encoding);
			out.write(bytes);
			out.flush();
		}
		else {
			conn.setRequestMethod("GET");
			conn.setDoOutput(false);
			conn.setDoInput(true);
			conn.connect();
		}

		in = conn.getInputStream();

		InputStreamReader isr = new InputStreamReader(in, encoding);
		BufferedReader br = new BufferedReader(isr);
		String buf = null;
		StringBuilder sb = new StringBuilder();

		while (null != (buf = br.readLine())) {
			sb.append(buf);
			sb.append("\r\n");
		}
		resBody = sb.toString();
		br.close();
		isr.close();

		return resBody;
	}

	// 서버로 정보를 전송한다.
	public synchronized void doPost(String url, HashMap<String, String> reqHeader, InputStream reqBody) 
		throws InterruptedException, IOException {
		URL u;
		OutputStream out = null;
		InputStream in = null;
		HttpURLConnection conn = null;

		u = new URL(url);

		if (0 == url.substring(0, 8).compareToIgnoreCase("https://")) {
			// HTTPS 신뢰할수 없는 사이트 무시 코드
			allowAllSSL();
			conn = (HttpsURLConnection) u.openConnection();
		} else {
			conn = (HttpURLConnection) u.openConnection();
		}

		for( Iterator<String> ikey = reqHeader.keySet().iterator(); ikey.hasNext(); ) {
			String key = ikey.next();
			conn.addRequestProperty(key, reqHeader.get(key));
		}
		if( reqBody != null ) {
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();
			
			out = new DataOutputStream(conn.getOutputStream());
			
			byte[] buf = new byte[2048];
			
			int nReaded = 0;
			
			while(true) {
				nReaded = reqBody.read(buf);
				if( nReaded < 0 ) {
					break;
				} else {
					out.write(buf, 0, nReaded);
					out.flush();
				}
			}
			
			out.close();
		}
		else {
			conn.setRequestMethod("GET");
			conn.setDoOutput(false);
			conn.setDoInput(true);
			conn.connect();
		}

		in = conn.getInputStream();

		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);
		String buf = null;
		StringBuilder sb = new StringBuilder();

		while (null != (buf = br.readLine())) {
			sb.append(buf);
			sb.append("\r\n");
		}
		br.close();
		isr.close();
	}
}
