[TOC]

# 了解Spring WebFlux原理

​	应大鹏哥的要求，学了一下WebFlux，从各方面的书籍、视频、官网和博客钻研了WebFlux的用法，和WebFlux的原理，花费几天时间进行了系统的梳理，于是有了如下的文章来帮助大家系统的了解WebFlux的原理和应用以及它的优缺点。

​	阅读本篇文章需要有Java8的Lambda表达式和Stream的基础或者对Reactive响应式编程有一定了解。

​	请认真阅读文章中的示例，必要时可以拷贝源码运行看看。

## 首先理解异步

​	



## 理解Reactive Stream

​	要想理解WebFlux就不得不说一下Reactive，Spring 5的WebFlux是基于Reactive Stream来实现的。理解了Reactive Stream原理可以帮助你快速明白WebFlux的原理和模式。

### Reactive Stream的几个重要概念

#### 背压

​	简而言之：**背压是在发布者端的队列，订阅者通知发布者让其减慢发布速度并保存消息防止订阅者速度跟不上**。

​	流处理机制中，`push`和`pull`模型是最常见的。在同步系统中发布者和订阅者的工作效率相当，发布者发布一个消息后阻塞，等待订阅者消费。订阅者消费完后，订阅者阻塞，等待发布者发布。但是这种处理方式效率低，如果使用异步处理消息。即发布和订阅速度不一样。

​	如果订阅者速度 > 发布者，会出现订阅者无消息可消费的情况。在同步数据处理机制中订阅者无限期等待，直到有消息可用。但在异步处理机制中，订阅者无需阻塞，继续处理其他任务即可。当出现了准备就绪的消息时，发布者会将它们异步发送给消费者。所以，在异步处理机制中，这种情况并不会对系统产生负面影响。

​	如果发布者速度 > 订阅者，有两种解决方案。

1. 改变订阅者。要么让订阅者拥有一个无边界缓冲区来保存快速传入的消息，要么让订阅者将它无法处理的消息丢弃。
2. 改变发布者。这类方案采用的策略即称为 **背压** 策略。订阅者通知发布者让其减慢发布速度并保存消息，直到订阅者准备好处理更多的消息。使用背压策略可以确保较快的发布者不会压制较慢的订阅者。但这个方案要求发布者要有无限制的缓冲区以确保发布者可以一直生产和保存消息。当然也可以实现有界缓冲区限制消息数量，但缓冲区满了就会放弃这些多出的消息。不过可以让发布者将放弃的消息再发布，知道订阅者将其订阅。

#### 反应式流

​	反应式流模型非常简单：订阅者向发布者发送多个元素的异步请求，发布者向订阅者异步发送多个或稍少的元素。反应式流会在pull模型和push模型流处理机制之间动态

​	

​	





```sequence
load() -> loadConfigurations(): 1s
loadConfigurations() -> loadUsers(): 2s
loadUsers() -> loadOrders(): 3s
```





```sequence
load() -> loadConfigurations(): 1s
load() -> loadUsers(): 2s
load() -> loadOrders(): 3s
```





### CompletableFuture

`Future`限制

- get()方法时阻塞的
- Future没有办法进行组合
  - 任务Future之间有依赖关系
    - 第一步的结果是第二步的输入

`CompletableFuture`

- 提供异步操作
- 提供Future链式操作
- 提供函数式编程



## 函数式编程 + Reactive

- Reactive Programming
- 编程风格
  - Fluent 流畅的
  - Streams 流式的
- 业务效果
  - 流程编排
  - 大多数业务逻辑是数据操作
- 函数式语言特性(Java 8+)
  - 消费类型 `Consumer`
  - 生产类型 `Supplier`
  - 转换类型 `Function`
  - 判断类型 `Predicate`
  - 提升/降低维度 `map`/`reduce`/`flatMap`

```java
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
```

​	但是Stream是`Iterator`模式，数据已经完全准备好了，拉(Pull)模式。

​	Reactive是观察者模式，来一个算一个，推(Push)模式，当有数据变化的时候，作出反应(Reactor)。

​	React(反应)



# WebFlux的介绍



## WebFlux和MVC对比

如下是

```java
@Slf4j
@RestController
public class DemoController {

    @GetMapping("/mvc")
    public String mvc(){
        log.info("mvc-start");
        doSomething("doSomething");
        log.info("mvc-end");
        return "mvc";
    }

    @GettMapping("/flux")
    public Mono<String> flux(){
        log.info("mvc-start");
        //Mono表示包含0个或1个响应的异步序列
        Mono<String> mono = Mono.fromSupplier(() -> doSomething("doSomething"));
        log.info("mvc-end");
        return mono;
    }

    private String doSomething(String message){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return message;
    }

}
```



![](http://img.shaking.top/webflux1.png)



## WebFLux简单应用



```java
@Slf4j
@RestController
public class DemoController {

     @GetMapping("/string")
    public Flux<String> fluxHandler(){
        //Mono表示包含0个或n个响应的异步序列
        return Flux.just("a", "b", "c");
    }

    @GetMapping("/array")
    public Flux<String> fluxHandler(@RequestParam String[] words){
        //将数组转换为Flux
        return Flux.fromArray(words);
    }

    @GetMapping("/list")
    public Flux<String> fluxHandler(@RequestParam List<String> words){
        //将List转换为Stream，再将Stream转为Flux
        return Flux.fromStream(words.stream());
    }
    
    @GetMapping("/time")
    public Flux<String> fluxTimeHandler(@RequestParam List<String> words){
        //对Flux的每个元素执行耗时操作
        log.info("flux-start");
        //和flux()方法对比，直接return和用返回值接受再return执行顺序是一样的
        log.info("flux-end");
        return Flux.fromStream(words.stream().map(t -> doSomething("element - " + t)));
    }

}
```



## 使用WebFlux进行开发

### Reactive Stream





## WebFlux使用场景

​	长期执行异步操作，一旦提交，慢慢操作。是否适合RPC操作？不适合

​	适合任务型的，少量线程，多个任务长时间运作，达到伸缩性。

​	`Mono`：单数据类似`Optional`，RxJava：`Single`

​	`Flux`：多数据集合`Collection`，RxJava：`Observable`

- 函数式编程
- 非阻塞(同步/异步)
- 远离Servlet API
  - API
    - `Servlet`
    - `HttpServletRequest`
- 不再强烈依赖Servlet API和容器
  - 容器
    - Tomcat
    - Jetty



## Spring Web Mvc复习

`HandlerInterceptor`：前置、后置处理器、完成阶段(异常处理)

- 前置：pre- before-
- 后置：post- after-
- 完成：类似`finally`
  - `org.springframework.web.servlet.HandlerInterceptor#afterCompletion`
  - `java.util.concurrent.CompletableFuture#whenComplete`

`HandlerMapping`：包含`HandlerInterceptor`集合



## 理解WebFlux实现原理

















