package com.lenovo.highavailablecommon;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 2017/11/15.
 */
public class TopFrame {

    public static void main(String[] args){

        Map<String,String> mapMQ = new HashMap<String,String>();

        Long threadId = 1L;

        Thread _FixedThread = findThread(threadId);

        _FixedThread.interrupt();


















    }


    public static Thread findThread(long threadId) {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while(group != null) {
            Thread[] threads = new Thread[(int)(group.activeCount() * 1.2)];
            int count = group.enumerate(threads, true);
            for(int i = 0; i < count; i++) {
                if(threadId == threads[i].getId()) {
                    return threads[i];
                }
            }
            group = group.getParent();
        }
        return null;
    }



}
