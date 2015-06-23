package com.demo.youtubeinvideoviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class YoutubeActivity extends Activity {

	String TAG = "YoutubeActivity";
	WebView webView;
	SeekBar seekBar;
	float totalVideoDuration;
	final static int MAX = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_youtube);
		
		seekBar = (SeekBar) this.findViewById(R.id.seekBar1);
		seekBar.setMax(MAX);
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				int progress = seekBar.getProgress();
				double secs = (progress * totalVideoDuration)/1000;
				secs = Math.ceil(secs);
				Log.d(TAG, "onStopTrackingTouch :: progress = " + progress +  "-- secs = " + secs);
				webView.loadUrl("javascript:m_seekVideo('"+ secs +"')");
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onStartTrackingTouch :: progress = " + seekBar.getProgress() );

			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				

			}
		});
		
		loadVideo();
	}
	
	private void loadVideo(){
		webView = (WebView) this.findViewById(R.id.webView);
		
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		
		
		webView.setWebChromeClient(new MyChromwClient());
		webView.setWebViewClient(new MyWebviewClient());
		webView.addJavascriptInterface(new MyJavaScriptInterface(this), "JSHandler");
		webView.loadUrl("file:///android_asset/ytplayer.html");
		//load(getCurrentFocus());
		
	}
	
	private void changeSlider(float time){
		float progress =  (time/totalVideoDuration) * 1000;
		final double d = Math.ceil(progress);
		Log.d(TAG, "changeSlider progress = " + d);
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				seekBar.setProgress((int)d);
			}
		});
	}
	
	private void modifySlider(final String flag){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(flag.equals("ENDED"))
					seekBar.setProgress(MAX);
			}
		});
	}
	
	public void load(View view){
		//dmLSM9zM0ME - 59 secs video
		//M7lc1UVf-VE - iframe API video
		webView.loadUrl("javascript:m_loadVideo('dmLSM9zM0ME')");
	}
	
	public void play(View view){
		webView.loadUrl("javascript:m_playVideo()");
	}
	
	public void pause(View view){
		webView.loadUrl("javascript:m_pauseVideo()");
		
	}
	
	public void stop(View view){
		webView.loadUrl("javascript:m_stopVideo()");
		
	}
	
	private class MyWebviewClient extends WebViewClient{
				
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
			
			Log.d(TAG, "onReceivedError : description = " + description);

		}
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			Log.d(TAG, "shouldOverrideUrlLoading : url = " + url);
			return true;
			
		}
	}
	
	private class MyChromwClient extends WebChromeClient{
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			// TODO Auto-generated method stub
//			Log.d(TAG, "consoleMessage : " + consoleMessage.message());
			return super.onConsoleMessage(consoleMessage);
		}
	}
	
	
	private class MyJavaScriptInterface{
		
		private YoutubeActivity activity;
		public MyJavaScriptInterface(YoutubeActivity activity){
			this.activity = activity;
		}
		
		public void setVideoDuration(String duration){
//			Log.d(TAG, "setVideoDuration = " + duration);
			
			try {
				changeSlider(Float.parseFloat(duration));	
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		
		public void setTotalVideoDuration(String duration){
			Log.d(TAG, "setTotalVideoDuration = " + duration);
			
			try {
				totalVideoDuration = Float.parseFloat(duration);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		public void videoEnd(){
			modifySlider("ENDED");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.youtube, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
