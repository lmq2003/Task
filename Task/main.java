package Task;

import java.util.concurrent.CountDownLatch;

public class main {

    public static void main(String[] args) {

        CountDownLatch signal_main=new CountDownLatch(3);
        CountDownLatch signal_others=new CountDownLatch(1);

        workers Worker_1 = new workers(signal_main,signal_others,"Worker-1");
        workers Worker_2 = new workers(signal_main,signal_others,"Worker-2");
        workers Worker_3 = new workers(signal_main,signal_others,"Worker-3");

        Worker_1.start();
        Worker_2.start();
        Worker_3.start();

        System.out.println("Main thread is doing some preparation...");

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            signal_main.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Main thread finished preparation, now releasing workers to start...");

        signal_others.countDown();

        System.out.println("Main thread continues to work after releasing workers.");


    }


}
