package com.rygz.tqueue;


import java.lang.ref.WeakReference;

import android.os.Handler;

/**
 * Thing
 * TODO:做事
 * @author:ZHL
 * @date:2017年4月26日
 */
public class TWorker implements Runnable{

	public static final int NORMAL = 1;
	public static final int AT_TIME = 2;
	public static final int DELAY = 3;

	private Doable thing;
	private WeakReference<TQueue> owner;

	private int type;
	private long time = 0;
	private boolean isUIThread = false;

	protected TWorker(TQueue queue, Doable thing){
		this(false, queue, thing);
	}

	protected TWorker(boolean isUIThread, TQueue queue, Doable thing){
		this(isUIThread, queue, thing, NORMAL, 0);
	}

	protected TWorker(TQueue queue, Doable thing, int type, long time){
		this(false, queue, thing, NORMAL, 0);
	}

	protected TWorker(boolean isUIThread, TQueue queue, Doable thing, int type, long time){
		super();
		this.isUIThread = isUIThread;
		this.owner = new WeakReference<TQueue>(queue);
		this.thing = thing;
		this.type = type;
		this.time = time;
	}

	protected void queueing(Handler handler){
		if (handler != null) {
			if (type == NORMAL) {
				handler.post(this);
			} else if (type == AT_TIME) {
				handler.postAtTime(this, time);
			}  else if (type == DELAY) {
				handler.postDelayed(this, time);
			} else {}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TWorker easyThing = (TWorker) o;
		return thing != null ? !thing.equals(easyThing.thing) : easyThing.thing != null;
	}

	@Override
	public int hashCode() {
		int result = thing != null ? thing.hashCode() : 0;
		return result;
	}

	@Override
	public void run(){
		if (owner == null || thing == null){
			return;
		}
		final TQueue queue = owner.get();
		if (queue != null && queue.isWorking()){
			if (isUIThread) {
				// TODO:切换到UIThread执行
				queue.getUIHandler().post(new Runnable() {
					@Override
					public void run() {
						try {
							thing.doing(queue, queue.getWorkerArgument());
						} catch (Exception e) {
							//Logdog.e("TWorker-run", e, "currentThread: " + Thread.currentThread().getName());
							handlerException(queue, e);
							return;
						}
					}
				});
			}else{
				// TODO:在HandlerThread中执行
				try {
					thing.doing(queue, queue.getWorkerArgument());
				} catch (final Exception e) {
					//Logdog.e("TWorker-run", e, "currentThread: " + Thread.currentThread().getName());
					queue.getUIHandler().post(new Runnable() {
						@Override
						public void run() {
							handlerException(queue, e);
						}
					});
				}
			}
		}
	}
	
	private void handlerException(final TQueue queue, Exception e) {
		Ceased error = queue.getCeased();
		if (error != null) {
			error.doing(e, queue.getWorkerArgument());
		}
	}
}
