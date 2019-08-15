package cn.itlou.rest.webflux;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WebFluxController {

    @RequestMapping("/index")
    public Mono<String> index(){
        println("执行计算");
        return Mono.fromSupplier(() -> {
            println("返回结果");
            return "Hello World";
        });
    }

    private static void println(Object message){
        System.out.printf("[线程:%s] %s\n", Thread.currentThread().getName(), message);
    }

}
