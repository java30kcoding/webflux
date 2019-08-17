package cn.itlou.rest.mvcandflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

/**
 * mvc和flux的对比
 */
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

    @GetMapping("/flux")
    public Mono<String> flux(){
        log.info("flux-start");
        //Mono表示包含1个或0个响应的异步序列
        Mono<String> mono = Mono.fromSupplier(() -> doSomething("doSomething")).subscribeOn(Schedulers.parallel());
        log.info("flux-end");
        return mono;
    }

    @GetMapping("/string")
    public Flux<String> fluxHandler(){
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

    @GetMapping(value = "/sse", produces = "text/event-stream")
    public Flux<String> sseHandler(){
        //将List转换为Stream，再将Stream转为Flux
        return Flux.just("a", "b", "c");
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
