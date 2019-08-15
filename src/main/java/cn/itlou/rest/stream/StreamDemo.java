package cn.itlou.rest.stream;

import java.util.stream.Stream;

public class StreamDemo {

    public static void main(String[] args) {

//写法非常直观
Stream.of(0,1,2,3,4,5,6,7,8,9)
        //获取奇数
        .filter(v -> v % 2 == 1)
        //奇数变偶数
        .map(v -> v -1)
        //聚合操作
        .reduce(Integer :: sum)
        //输出0 + 2 + 4 + 6 + 8
        .ifPresent(System.out :: println);
    }


}
