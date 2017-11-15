package com.lenovo.highavailablecommon.test.threadpackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyList {

    private static List<String> list = new ArrayList<String>();

    public static void add() {
        list.add("anyString");
    }

    public static int size() {
        return list.size();
    }
}


class ThreadA extends Thread {

    private Object lock;

    public ThreadA(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                System.out.print(lock.toString());
                if (MyList.size() != 5) {
                    System.out.println("wait begin "
                            + System.currentTimeMillis());
                    lock.wait();
                    System.out.println("wait end  "
                            + System.currentTimeMillis());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class ThreadB extends Thread {
    private Object lock;

    private String lockstr;

    public ThreadB(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    System.out.println("ThreadB is adding .....");
                    MyList.add();
                    if (MyList.size() == 5) {
                        //lock = "123";
                        lock.notify();
                        System.out.println("已经发出了通知");
                    }
                    System.out.println("添加了" + (i + 1) + "个元素!");
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Run {

    public static void main(String[] args) {

        try {

            Object lock = new Object();

            ThreadA _ThreadWatch = new ThreadA(lock);

            _ThreadWatch.start();

            Thread.sleep(50);

            ThreadB _ThreadB = new ThreadB(lock);

            _ThreadB.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}