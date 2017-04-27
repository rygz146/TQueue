# TQueue

> 一个Android开发库, 可以任意切换线程的链式调用任务队列, 可添加定时, 延时任务, 统一异常处理(Ceased中断),但不影响整个任务链的运行.

> 思路借鉴自AndroidOperationQueue. 地址如下: https://github.com/skyfe79/AndroidOperationQueue

### 具体用法如下:

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


## MIT License

The MIT License (MIT)

Copyright (c) 2016 Sungcheol Kim, [https://github.com/rygz146/TQueue](https://github.com/rygz146/TQueue.git)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
