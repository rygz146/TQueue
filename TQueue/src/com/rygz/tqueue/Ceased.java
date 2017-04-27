package com.rygz.tqueue;

import android.os.Bundle;

/**
 * Ceased
 * TODO:中止
 * 说明: 
 * 1.整个TQueue只有一个Ceased;
 * 2.所有的Ceased都会发送到在主线程处理;
 * 3.每个Thing在执行中的Exception都会在此接口处理;
 * 4.对Ceased处理完后会直接进行下一个任务,异常抛出不影响整个队列thing的进行.
 * @author:ZHL
 * @date:2017年4月27日
 */
public interface Ceased {
	/***
	 * TODO:处理中断
	 * @param error
	 * @param args
	 */
	void doing(Throwable error, Bundle args);
}
