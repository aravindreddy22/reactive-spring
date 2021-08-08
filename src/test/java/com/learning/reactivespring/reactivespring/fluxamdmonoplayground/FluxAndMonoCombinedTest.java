package com.learning.reactivespring.reactivespring.fluxamdmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class FluxAndMonoCombinedTest {

    @Test
    public void combineUsingMerge(){
        Flux<String> flux1= Flux.just("A","B","C");
        Flux<String> flux2= Flux.just("D","E","F");
        Flux<String> mergeFlux= Flux.merge(flux1,flux2).log();

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_withDelay(){
        VirtualTimeScheduler.getOrSet();
        Flux<String> flux1= Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2= Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));
        Flux<String> mergeFlux= Flux.merge(flux1,flux2).log();

        /*StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();*/
        StepVerifier.withVirtualTime(()->mergeFlux)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                .expectNextCount(6)
                .verifyComplete();
    }


    @Test
    public void combineUsingMerge_withConcat(){
        Flux<String> flux1= Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2= Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));
        Flux<String> mergeFlux= Flux.concat(flux1,flux2).log();

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }


    @Test
    public void combineUsingMerge_withZip(){
        Flux<String> flux1= Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2= Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));
        Flux<String> mergeFlux= Flux.zip(flux1,flux2,(t1,t2)->t1.concat(t2));

        StepVerifier.create(mergeFlux)
                .expectSubscription()
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }
}
