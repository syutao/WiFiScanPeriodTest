package com.test.wifiscanfreq;

// 需要 4.2 版本android支持。

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import com.example.wifitimestamp.R;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView tvX;
	private WifiManager wifiManager;
	final Handler handler = new Handler();

	final static String TAG = "Main"; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvX= (TextView)findViewById(R.id.textView1);
		wifiManager = (WifiManager)getSystemService("wifi");
		
		

		//		wifiManager.startScan();
		handler.post(wifiRecording);

	}

	static List<ScanResult> preList;
	static boolean finital = false;
	

	private boolean wifiScanUnchanged() {
		List<ScanResult> list = wifiManager.getScanResults();
		
		if(!finital){
			preList = wifiManager.getScanResults();
			finital = true;
		}

		if(isScanResultlistEqual(list))
		{
			Log.i(TAG, "scanResult of wifi equal to perious");
			list.clear();
			return true;
		}
		else{
			preList = list;
			
			Log.e(TAG, "scanResult of wifi not equal to perious");
			list.clear();
			return false;
		}
	}

	private boolean isScanResultlistEqual(List<ScanResult> list){

		int listlen = list.size();
		int prelistlen =preList.size();

		if(listlen != prelistlen){
			
			Log.v(TAG,"length of list is "+String.valueOf(listlen));
			return false;
		}else{
			for(int i=0;i<listlen;i++){
				if(list.get(i).level!=preList.get(i).level)
				{
					Log.v(TAG, "level of wifi not equal to perious");
					return false;
				}

				if(!list.get(i).BSSID.equals(preList.get(i).BSSID))
				{
					Log.v(TAG, "BSSID of wifi not equal to perious");
					return false;
				}
			}
			
			return true;
		}
		
	}


	static int secStatic =0;

	private Runnable wifiRecording = new Runnable(){

		@Override
		public void run() {

			long time = System.currentTimeMillis();
			final Calendar mCalendar = Calendar.getInstance();
			mCalendar.setTimeInMillis(time);
			int minuts = mCalendar.get(Calendar.MINUTE);
			int second = mCalendar.get(Calendar.SECOND);
			int millisec = mCalendar.get(Calendar.MILLISECOND);

			try {


				if(!wifiScanUnchanged()){
					// changed
					
					secStatic =0;
					wifiManager.startScan();
					System.out.println(String.valueOf(minuts)+":"+String.valueOf(second)
							+"."+String.valueOf(millisec));
				}
				else {
					secStatic = secStatic +1;
				}
				
				if(6 == secStatic){
					wifiManager.startScan();
				}
				handler.postDelayed(this, 10);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	};

	public String[] getCpuInfo() { 
		String str1 = "/proc/cpuinfo"; 
		String str2=""; 
		String[] cpuInfo={"",""}; 
		String[] arrayOfString; 
		try { 
			FileReader fr = new FileReader(str1); 
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192); 
			str2 = localBufferedReader.readLine(); 
			arrayOfString = str2.split("\\s+"); 
			for (int i = 2; i < arrayOfString.length; i++) { 
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " "; 
			} 
			str2 = localBufferedReader.readLine(); 
			arrayOfString = str2.split("\\s+"); 
			cpuInfo[1] += arrayOfString[2]; 
			localBufferedReader.close(); 
		} catch (IOException e) { 
		} 
		return cpuInfo; 
	} 


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
