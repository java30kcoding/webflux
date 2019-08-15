package cn.itlou.rest.completablefuture;

import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class ComplatableFutureDemo {

    public static void main(String[] args) {

        println("当前线程");

        CompletableFuture.supplyAsync(() -> {
            println("第一步返回Hello");
            return "Hello";
        }).thenApplyAsync(result -> {
            println("第二步是第一步的结果+World");
            return result + " World";
        }).thenAccept(ComplatableFutureDemo :: println)
        .join();//等待线程结束

    }

    private static void println(String message){
        System.out.printf("[线程:%s] %s\n", Thread.currentThread().getName(), message);
    }

}
