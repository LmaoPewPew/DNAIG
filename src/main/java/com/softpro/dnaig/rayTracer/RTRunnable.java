package com.softpro.dnaig.rayTracer;

import javafx.concurrent.Task;

public class RTRunnable implements Runnable {

    private final Task<Void> task;
    CallbackInterface callback;

    public RTRunnable(Task<Void> task, CallbackInterface callback) {
        this.task = task;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            task.run();
            //System.out.println("tgestaskjd");
        } catch (Exception ex) {
            Thread t = Thread.currentThread();
            t.getUncaughtExceptionHandler().uncaughtException(t, ex);
            System.out.println("exception");
        }
        callback.taskDone("tesfjndslöfj");
    }
}
