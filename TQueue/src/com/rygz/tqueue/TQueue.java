package com.rygz.tqueue;

import java.util.concurrent.LinkedBlockingQueue;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

/**
 * TQueue
 * TODO:事件队列
 * @author:ZHL
 * @date:2017年4月26日
 */
public class TQueue {

	private Handler uiHandler;
	private Handler workerHandler;
	private Bundle workerArgument;
	private HandlerThread workerThread;
	private Ceased errorHandler;

	private LinkedBlockingQueue<TWorker> workerQueue = new LinkedBlockingQueue<TWorker>();

	private String name = "TQueue";
	private boolean isWorking = false;
	private int priority = Process.THREAD_PRIORITY_DEFAULT;
	
	public TQueue(String name) {
		this(name, Process.THREAD_PRIORITY_DEFAULT);
	}

	public TQueue(String name, int priority) {
		super();
		this.name = name;
		this.priority = priority;
		this.workerArgument = new Bundle();
		this.workerThread = new HandlerThread(name, priority);
	}
	
	public static TQueue queue(){
		return queue("ThingQueue");
	}

	public static TQueue queue(String name){
		return new TQueue(name);
	}
	

	public TQueue go(Ceased ceased){
		errorHandler = ceased;
		if (!isWorking()){
			isWorking = true;
			if (workerThread == null){
				workerThread = new HandlerThread(name, priority);
			}
			workerThread.start();
			workerHandler = new Handler(workerThread.getLooper());
			for (TWorker thing : workerQueue) {
				thing.queueing(workerHandler);
			}
			workerQueue.clear();
		}
		return this;
	}
	
	public TQueue go(){
		return go(null);
	}

	public TQueue rest(final long restTimeMillis) {
		on(new Doable() {
			@Override
			public void doing(TQueue queue, Bundle args) {
				try {
					Thread.sleep(restTimeMillis);
				} catch (Exception e) {}
			}
		});
		return this;
	}
	
	public TQueue onUI(Doable thing){
		if (thing != null){
			TWorker work = new TWorker(true, this, thing);
			if (isWorking){
				if (workerHandler != null){
					workerHandler.post(work);
				}
			}else{
				workerQueue.add(work);
			}
		}
		return this;
	}

	public TQueue onUIAtTime(long time, Doable thing){
		if (thing != null){
			TWorker work = new TWorker(true, this, thing, TWorker.AT_TIME, time);
			if (isWorking){
				if (workerHandler != null){
					workerHandler.postAtTime(work, time);
				}
			}else{
				workerQueue.add(work);
			}
		}
		return this;
	}

	public TQueue onUIDelayed(long time, Doable thing){
		if (thing != null){
			TWorker work = new TWorker(true, this, thing, TWorker.DELAY, time);
			if (isWorking){
				if (workerHandler != null){
					workerHandler.postDelayed(work, time);
				}
			}else{
				workerQueue.add(work);
			}
		}
		return this;
	}
	
	public TQueue on(Doable thing){
		if (thing != null){
			TWorker work = new TWorker(this, thing);
			if (isWorking){
				if (workerHandler != null){
					workerHandler.post(work);
				}
			}else{
				workerQueue.add(work);
			}
		}
		return this;
	}
	
	public TQueue onAtTime(long time, Doable thing){
		if (thing != null){
			TWorker work = new TWorker(this, thing, TWorker.AT_TIME, time);
			if (isWorking){
				if (workerHandler != null){
					workerHandler.postAtTime(work, time);
				}
			}else{
				workerQueue.add(work);
			}
		}
		return this;
	}

	public TQueue onDelayed(long time, Doable thing){
		if (thing != null){
			TWorker work = new TWorker(this, thing, TWorker.DELAY, time);
			if (isWorking){
				if (workerHandler != null){
					workerHandler.postDelayed(work, time);
				}
			}else{
				workerQueue.add(work);
			}
		}
		return this;
	}

	public void off(Doable thing){
		if (thing != null){
			TWorker work = new TWorker(this, thing);
			if (isWorking){
				if (workerHandler != null){
					workerHandler.removeCallbacks(work);
				}
			}else{
				workerQueue.remove(work);
			}
		}
	}

	public void off(){
		if (workerHandler != null){
			workerHandler.removeCallbacksAndMessages(null);
		}
		if (uiHandler != null) {
			uiHandler.removeCallbacksAndMessages(null);
		}
		workerQueue.clear();
	}

	@SuppressLint("NewApi")
	public void release(){
		try {
			isWorking = false;
			workerThread.quitSafely();
			workerThread = null;
			workerHandler = null;
			uiHandler = null;
			workerArgument.clear();
		} catch (Exception e) {}
	}

	public Bundle getWorkerArgument() {
		return workerArgument;
	}
	
	public Handler getUIHandler() {
		if (uiHandler == null) {
			uiHandler = new Handler(Looper.getMainLooper());
		}
		return uiHandler;
	}
	
	public boolean isWorking(){
		return isWorking;
	}

	public Ceased getCeased() {
		return errorHandler;
	}

	public void ceased(Ceased ceased) {
		this.errorHandler = ceased;
	}
}
