package com.softpro.dnaig.rayTracer;


import com.softpro.dnaig.Output;
import com.softpro.dnaig.utils.Config;
import com.softpro.dnaig.utils.Vector3D;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.Arrays;

public class RayTracer implements CallbackInterface {

    static Camera camera;
    int n = 0;
    private int threadsFinished = 0;
    private long startTime;

    private final Thread[] threads = new Thread[Config.THREADS];


    int current = -1;
    public synchronized int getCurrent() {
        return ++current;
    }

    public void trace() throws IOException {
        startTime = System.currentTimeMillis();
        camera = CustomScene.getScene().camera;

        int work_w = Output.WIDTH / Config.TILES;
        int work_h = Output.HEIGHT / Config.TILES;

        for (int a = 0; a < Config.THREADS; a++) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws IOException {

                    int work = getCurrent();
                    int tid = getCurrent();
                    //System.out.printf("Got thread id %d\n", tid);

                    int start_x = work_w * (work % Config.TILES);
                    int start_y = work_h * (work / Config.TILES);

                    while (work < Config.TILES*Config.TILES) {
                        //System.out.printf("Thread %d working on task %d\n", tid, work);

                        for (int i = start_x; i < start_x + work_w; i++) {
                            //System.out.println(i);
                            //System.out.printf("Task %d: work=%d i=%d\n", tid, work, i);
                            for (int j = start_y; j < start_y + work_h; j++) {
                                //System.out.println(j);
                                //System.out.printf("Task %d: work=%d i=%d j=%d\n", tid, work, i, j);
                                // check if RayTracer thread was asked to cancel
                                if (Output.getOutput().isThreadCancelled())
                                    return null;

                                //System.out.printf("Task %d: work=%d i=%d j=%d no cancel\n", tid, work, i, j);

                                double u= camera.getL() + i + 0.5;
                                //u = u/200;
                                double v= camera.getT() - (j+0.5);
                                //v = v/200;
                                Vector3D s = Util.add(camera.getU().scalarMultiplication(u), camera.getV().scalarMultiplication(v), camera.getW_d_negated());
                                //Vector3D s = Util.add(camera.getU().scalarMultiplication(u), camera.getV().scalarMultiplication(v), camera.getScreen()).subtract(camera.getEye());
                                Vector3D dir = s.normalize();

                                //System.out.printf("Task %d: work=%d i=%d j=%d dir\n", tid, work, i, j);

                                n++;
                                Ray r = new Ray(camera.getEye(), dir);

                                //System.out.printf("Task %d: work=%d i=%d j=%d ray\n", tid, work, i, j);

                                int res_color = r.castPrimary(0);

                                //System.out.printf("Task %d: work=%d i=%d j=%d cast\n", tid, work, i, j);

                                Output output = Output.getOutput();

                                //System.out.printf("Task %d: work=%d i=%d j=%d output\n", tid, work, i, j);

                                try {
                                    output.setPixelTest(tid, i, j, res_color, work);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //System.out.printf("Task %d: work=%d i=%d j=%d pixel\n", tid, work, i, j);
                            }
                        }
                        //System.out.printf("Thread %d finished work %d\n", tid, work);
                        work = getCurrent();
                        start_x = work_w * (work % Config.TILES);
                        start_y = work_h * (work / Config.TILES);
                        //throw new RuntimeException("test");
                        //System.out.printf("got new work at x: %d y: %d\n", start_x, start_y);
                    }
                    return null;
                }
            };
            RTRunnable runnable = new RTRunnable(task, this);
            Thread thread = new Thread(runnable);
            threads[a] = thread;
        }
        for (Thread thread : threads) {
            System.out.println("starting thread");
            thread.start();
        }
    }

    @Override
    public void taskDone(String details) {
        threadsFinished++;

        if (threadsFinished >= Config.THREADS) {
            System.out.println("Time: " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
        }
    }
}