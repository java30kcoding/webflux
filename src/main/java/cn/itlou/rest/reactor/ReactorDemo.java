package cn.itlou.rest.reactor;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class ReactorDemo {

    public static void main(String[] args) {
        //直接执行
        Flux.just(0,1,2,3,4,5,6,7,8,9)
                //获取奇数
                .filter(v -> v % 2 == 1)
                //奇数变偶数
                .map(v -> v -1)
                //聚合操作
                .reduce(Integer :: sum)

                .subscribeOn(Schedulers.parallel())
                //订阅才执行
                .subscribe(ReactorDemo :: println);
    }

    private static void println(Object message){
        System.out.printf("[线程:%s] %s\n", Thread.currentThread().getName(), message);
    }

}
