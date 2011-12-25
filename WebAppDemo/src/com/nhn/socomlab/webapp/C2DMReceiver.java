package com.nhn.socomlab.webapp;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class C2DMReceiver extends BroadcastReceiver {

	String rid = "";
	String msisdn = "";
	String serverUrl = "http://testtomcat.cafe24.com/UpdateInfo";

	public static void c2dmRegister(Context c) {
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(c, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", "socomapp@gmail.com");

		c.startService(registrationIntent);
	}

	public static void c2dmUnregister(Context c) {
		Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
		unregIntent.putExtra("app", PendingIntent.getBroadcast(c, 0, new Intent(), 0));
		c.startService(unregIntent);
	}

	@Override
	public void onReceive(Context c, Intent intent) {
		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
			handleRegistration(c, intent);
		} else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
			handleMessage(c, intent);
		}
	}

	private void handleRegistration(Context c, Intent intent) {
		String registration = intent.getStringExtra("registration_id");
		if (intent.getStringExtra("error") != null) {
			alert(c, "handleRegistration : error");
		} else if (intent.getStringExtra("unregistered") != null) {
			alert(c, "handleRegistration : unregistered");
		} else if (registration != null) {
			alert(c, "handleRegistration : " + registration);

			msisdn = ((TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
			if (null == msisdn) {
				msisdn = ((WifiManager)c.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
			}

			rid = registration;
			new Thread(new Runnable() {
				@Override
				public void run() {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("msisdn", msisdn);
					map.put("rid", rid);

					HttpUtil http = new HttpUtil();
					try {
						http.doPost(serverUrl, map, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	private void handleMessage(Context c, Intent intent) {
		String msg = intent.getStringExtra("msg");

		try {
			JSONObject json = new JSONObject(msg);
			if (0 == "url".compareToIgnoreCase(json.getString("cmd"))) {
				Intent i = new Intent(c, WebAppActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("url", json.getString("url"));
				c.startActivity(i);
			} else if (0 == "intent".compareToIgnoreCase(json.getString("cmd"))) {
			}
		} catch (Exception e) {
			alert(c, msg);
		}

	}

	private void alert(Context c, String msg) {
		Log.i("C2DM", "alert : " + msg);
		Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
	}
}