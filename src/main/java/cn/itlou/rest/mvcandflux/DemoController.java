package cn.itlou.rest.mvcandflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * mvc和flux的对比
 */
@Slf4j
@RestController
public class DemoController {

    @RequestMapping("/mvc")
    public String mvc(){
        log.info("mvc-start");
        doSomething("doSomething");
        log.info("mvc-end");
        return "mvc";
    }

    @RequestMapping("/flux")
    public Mono<String> flux(){
        log.info("flux-start");
        Mono<String> mono = Mono.fromSupplier(() -> doSomething("doSomething"));
        log.info("flux-end");
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
