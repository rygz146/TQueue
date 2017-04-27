package com.rygz.tqueue;

import android.os.Bundle;

/**
 * Doable
 * TODO:可做的事件
 * @author:ZHL
 * @date:2017年4月26日
 */
public interface Doable {

    void doing(TQueue queue, Bundle args);
}
