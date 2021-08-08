package com.learning.reactivespring.reactivespring.fluxamdmonoplayground;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {
    @Test
    public void fluxTest(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
               // .concatWith(Flux.error(new RuntimeException(" Exception occurred!!!")))
                .concatWith(Flux.just("After Error"));
        stringFlux
                .subscribe(System.out::println,
                        (e) -> System.err.println(e),
                        () -> System.out.println("completed!!!"));

        // Testing the flux
    }
    @Test
    public void fluxTestElements_withOutError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete();
    }
    @Test
    public void fluxTestElements_withError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred!!!")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
              //  .expectError(RuntimeException.class)
                .expectErrorMessage("Exception occurred!!!")
                .verify();
                 //.verifyComplete();
    }

    @Test
    public void fluxTestElements_withError1(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred!!!")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring","Spring Boot","Reactive Spring")
                .expectErrorMessage("Exception occurred!!!")
                .verify();
        //.verifyComplete();
    }
    @Test
    public void fluxTestElementsCount_withError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception occurred!!!")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectErrorMessage("Exception occurred!!!")
                .verify();
    }

    //MONO - it is only one Elements
    @Test
    public void monoTest(){
        Mono<String> stringMono= Mono.just("Spring");
        StepVerifier.create(stringMono.log())
                .expectNext("Spring")
                .verifyComplete();
    }
    @Test
    public void monoTest_Error(){
        Mono<String> stringMono= Mono.error(new RuntimeException("Exception Occurred!!"));
        StepVerifier.create(stringMono.log())
               //.expectError(RuntimeException.class)
               .expectErrorMessage("Exception Occurred!!")
                .verify();
    }
}
