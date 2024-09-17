# Task

## 一 题目

请设计并实现一个多线程程序，要求启动 3 个工作线程，这些线程在接收到启动信号之前必须处于等待状态，直到主线程完成某项准备工作后，通过一个信号同时通知这 3 个工作线程开始执行各自的任务。

### 要求：
1. 主线程在启动工作线程后，需要执行一些准备工作。准备工作完成后，主线程应发出信号，通知所有等待的线程同时开始工作。
2. 每个工作线程在收到信号后，执行一段任务。任务包括：
   - 打印任务开始的信息。
   - 模拟任务执行时间（可通过 `Thread.sleep()` 模拟不同时间的任务）。
   - 打印任务结束的信息。
3. 主线程在发出信号后，继续执行其后续的任务。

### 打印输出要求：

- 每个工作线程启动后，首先打印信息表示线程已准备好并在等待信号。
- 主线程在执行准备工作时，打印一条表示准备工作的消息。
- 当主线程发出开始信号时，打印信息表示所有线程可以开始工作。
- 每个工作线程在收到信号后，打印任务开始的消息，并在任务完成后打印任务结束的消息。

### 打印格式示例：

```
Worker-1 is ready and waiting for the start signal.
Worker-2 is ready and waiting for the start signal.
Worker-3 is ready and waiting for the start signal.
Main thread is doing some preparation...
Main thread finished preparation, now releasing workers to start...
Main thread continues to work after releasing workers.
Worker-1 starts working.
Worker-2 starts working.
Worker-3 starts working.
Worker-2 finished work.
Worker-1 finished work.
Worker-3 finished work.
```

### 注意：
1. 所有工作线程在收到主线程的启动信号前都应该处于等待状态，不允许提前开始任务。
2. 主线程的准备工作可以通过模拟（例如 `Thread.sleep()`）来体现。
3. 任务完成时间可以设为不同的随机值，使得工作线程完成的顺序不固定。

### 目标：
通过这道题目，考察你对多线程同步、线程间通信机制的掌握。



## 二 完成代码

```java
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

```



## 三 思路

使用`CountDownLatch`来控制工作线程的启动。在此任务中设置了两个`CountDownLatch`，分别为signal_main与signal_others。signal_main用于控制主线程，而signal_others用于控制其他线程。在主线程中用signal_others调度工作线程，在工作线程中使用signal_main共同调度主线程。



## 四 效果

![](C:\Users\Lenovo\Documents\WeChat Files\wxid_k26rx1mc66yu12\FileStorage\Temp\c88258e346f8deae8ee0437ff59a243.png)