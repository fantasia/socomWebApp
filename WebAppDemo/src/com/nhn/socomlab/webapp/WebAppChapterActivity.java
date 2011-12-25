package com.nhn.socomlab.webapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WebAppChapterActivity extends Activity implements View.OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		LinearLayout ll = (LinearLayout)findViewById(R.id.root);

		ll.addView(makeActivityButton("WebApp01", WebApp01Activity.class));
		ll.addView(makeActivityButton("WebApp02", WebApp02Activity.class));
		ll.addView(makeActivityButton("WebApp03", WebApp03Activity.class));
		ll.addView(makeActivityButton("WebApp04", WebApp04Activity.class));
		ll.addView(makeActivityButton("WebApp05", WebApp05Activity.class));
		ll.addView(makeActivityButton("WebApp06", WebApp06Activity.class));

		ll.addView(makeActivityButton("WebAppScroll", WebAppScrollActivity.class));

		ll.addView(makeActivityButton("WebAppCacheTest1", WebAppCacheTest1Activity.class));
		ll.addView(makeActivityButton("WebAppCacheTest2", WebAppCacheTest2Activity.class));
	}

	private View makeActivityButton(String name, final Class activity) {

		Button btn = new Button(this);
		btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		btn.setText(name);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), activity);
				startActivity(i);
			}
		});

		return btn;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		WebView wv = new WebView(this);
		wv.clearCache(true);
		Toast.makeText(this, "Clear Cache", Toast.LENGTH_SHORT).show();
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.c2dm_reg:
				C2DMReceiver.c2dmRegister(this);
				break;

			case R.id.c2dm_unreg:
				C2DMReceiver.c2dmUnregister(this);
				break;

			case R.id.webapp:
				Intent i = new Intent(getApplicationContext(), WebAppActivity.class);
				startActivity(i);
				
			default:
				break;
		}
	}
}