package com.myown.application.threads;

/**
 * Created by frode on 05.10.2016.
 */
public class TooManyThreads {

    public static void main( String[] args ) throws Exception
    {
        System.out.println( "Hello World!" );

        for(int i = 0; i < 3000; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
