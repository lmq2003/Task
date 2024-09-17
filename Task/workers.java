package Task;

import java.util.concurrent.CountDownLatch;
import java.util.Random;

public class workers extends Thread{
    private final CountDownLatch signal_main ;
    private final CountDownLatch signal_others ;
    public String name;

    public workers(CountDownLatch signal_main,CountDownLatch signal_others,String name){

        this.signal_main =signal_main;
        this.signal_others =signal_others;
        this.name = name;
    }

    Random random = new Random();

    // 定义随机数的范围
    int min = 2000;
    int max = 5000;

    // 生成随机数
    int randomNumber = random.nextInt((max - min) + 1) + min;


    @Override
    public void run(){




        System.out.println(this.name + " is ready and waiting for the start signal");

        signal_main.countDown();

        try {
            signal_others.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(this.name + " starts working");

        try {
            Thread.sleep(randomNumber);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(this.name + " finished work");


    }






}
