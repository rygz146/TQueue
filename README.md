# TQueue

> Android 可以任意切换线程的任务队列, TQueue

> 思路借鉴自AndroidOperationQueue.

> 地址如下: https://github.com/skyfe79/AndroidOperationQueue.git

```java
TQueue.queue("TQueue").on(new Doable() {
			@Override
			public void doing(TQueue queue, Bundle args) {
				//on操作可以开启一个及时异步任务
				//当执行完成该任务后会自动执行下一个任务
				//如果出现异常,在会调go操作中的Ceased(中断)的doing方法
			}
		}).rest(1000)//在此休息片刻
		.onUI(new Doable() {
			@Override
			public void doing(TQueue queue, Bundle args) {
				//onUI操作可以开启一个在主线程中执行的任务
			}
		}).onUIDelayed(long delayedTime, new Doable() {
			@Override
			public void doing(TQueue queue, Bundle args) {
				//delayedTime: 延时时间
				//开启一个在ui线程中的延时任务
				//延时任务和定时任务的调度依赖Hander处理
			}
		}).onDelayed(long delayedTime, new Doable() {
			@Override
			public void doing(TQueue queue, Bundle args) {
				//开启一个工作线程的延时任务
			}
		}).onUIAtTime(long atTime, new Doable() {
			@Override
			public void doing(TQueue queue, final Bundle args) {
				//开启一个UI线程的定时任务
			}
		}).onAtTime(long atTime, new Doable() {
			@Override
			public void doing(TQueue queue, final Bundle args) {
				//开启一个工作线程的定时任务
			}
		}).go(new Ceased() {
			@Override
			public void doing(Throwable error, Bundle args) {
				//上面任务中出现的异常都在此处理
				/**说明: 
				 * 1.整个TQueue只有一个Ceased;
				 * 2.所有的Ceased都会发送到在主线程处理;
				 * 3.每个任务在执行中的Exception都会在此接口处理;
				 * 4.对Ceased处理完后会直接进行下一个任务,异常抛出不影响整个队列任务的进行.
				 */
			}
		});
```

