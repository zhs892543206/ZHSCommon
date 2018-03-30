package com.zhs.commonhelper.runnable;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 计时器线程
 * @author zhs
 *
 */
public class TimingRunnable implements Runnable {
	private int time;
	private TimingInterface tInterface;
	private boolean isRepeat;
	private boolean isTrue = true;
	private boolean isInterrupted = false;
	private boolean isMainThread;
	
	private boolean isSleepAfter = false;//是否先执行再睡眠
	/**
	 * 
	 * @param time 延迟时间
	 * @param isRepeat 是否重复执行延迟处理
	 * @param isMainThread 回调tInterface是否在主线程中执行
	 * @param timingInterface 时间到了后进行的处理
	 * 
	 */
	public TimingRunnable(int time, boolean isRepeat, boolean isMainThread, TimingInterface timingInterface){
		this.time = time;
		this.tInterface = timingInterface;
		this.isRepeat = isRepeat;
		this.isMainThread =isMainThread;
	}
	
	/**
	 * 
	 * @param time
	 * @param isRepeat
	 * @param isMainThread
	 * @param isSleepAfter true 先执行再sleep，false先sleep再执行
	 * @param timingInterface
	 */
	public TimingRunnable(int time, boolean isRepeat, boolean isMainThread, Boolean isSleepAfter, TimingInterface timingInterface){
		this.time = time;
		this.tInterface = timingInterface;
		this.isRepeat = isRepeat;
		this.isMainThread =isMainThread;
		this.isSleepAfter = isSleepAfter;
	}
	
	@Override
	public void run(){
		
		while(isTrue && !isInterrupted){ 
		    try{ 
		    	isTrue = isRepeat;
		    	if(!isSleepAfter && time>0){
					Thread.sleep(time); // sleep 1000ms
					
		    	}
		    	if(isInterrupted){
		    		break;
		    	}
		    	if(isMainThread){
		    		/*
		    		 * 在cqm_v2_androidpad项目中MyTaskOpera的connect里面使用这个类会奔溃，
		    		 * 原因是connect是在application创建就运行的。所以这个时候创建handler就会奔溃
		    		 * 而后来试着写在注释掉的这里也不行会导致ui线程不能跳转。所以就多了个TimingNoHandleRunnable类
		    		 */
//		    		Handler handler = new Handler(){
//		    			   public void handleMessage(Message msg){ 
//		    				   switch (msg.what) { 
//		    				 
//		    				   } 
//		    			 	 super.handleMessage(msg); 
//		    			  }
//		    		   }; 
			    	Message message = new Message();
					message.what = 1; 
					handler.sendMessage(message); 
		    	}else{
		    		if(tInterface!=null){
			    		   tInterface.setTimeOverListener();
			    	   }
		    	}
		    	if(isSleepAfter && time>0){
					Thread.sleep(time); // sleep 1000ms
					
		    	}
		    	if(isInterrupted){
		    		break;
		    	}
			}catch (Exception e) {
			} 	
		} 
	} 
	
	
	Handler handler = new Handler(Looper.getMainLooper()){
		   public void handleMessage(Message msg){
			   switch (msg.what) { 
			       case 1: 
			    	   if(tInterface!=null){
			    		   tInterface.setTimeOverListener();
			    	   }
			 
			   } 
		 	 super.handleMessage(msg); 
		  }
	   }; 


   public interface TimingInterface{

	   public void setTimeOverListener();
   }
   
   /**
    * 关闭TimingRunnable
    */
   public void stopTimingRunnable(){
	   isInterrupted = true;
   }
}
