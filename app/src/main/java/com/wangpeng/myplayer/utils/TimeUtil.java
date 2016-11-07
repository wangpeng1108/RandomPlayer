package com.wangpeng.myplayer.utils;

import android.R.integer;
import android.util.Log;

 

public class TimeUtil {

	public static String mill2mmss(long duration){
		int m,s;
		String str = "";
		
		int x=(int)duration/1000;
		s=x%60;
		m=x/60;
		if(m<10){
			str+="0"+m;
		}else{
			str+=m;
		}
		
		if(s<10){
			str+=":0"+s;
		}else{
			str+=":"+s;
		}
		
		return str;
	}
	
	public static int getLrcMillTime(String time){
		if(time==null||time.length()==0)return -1;
		int millTime=0;
		time=time.replace(".", ":");
		String timedata[]=time.split(":");
		int min=0;
		int second=0;
		int mill=0;
		try {
			min = Integer.parseInt(timedata[0]);
			second = Integer.parseInt(timedata[1]);
			mill = Integer.parseInt(timedata[2]);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		millTime=(min*60+second)*1000+mill*10;
		return millTime;
	}

	public static String timeToString (int time){
		String timestring;
		int minutes=(time/1000)/60;
		int seconds=(time/1000)%60;
		String min=String.valueOf(minutes);
		String sec;
		if (seconds<10){
			sec="0"+String.valueOf(seconds);
		}else {
			sec = String.valueOf(seconds);
		}
		timestring=min+":"+sec;
		return timestring;
	}
}
