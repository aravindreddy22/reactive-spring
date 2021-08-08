package com.learning.reactivespring.reactivespring.fluxamdmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {
    @Test
    public void infiniteTest() throws InterruptedException {
        Flux<Long> infiniteFlux= Flux.interval(Duration.ofMillis(100))
                .log();
        infiniteFlux.subscribe((element) -> System.out.println("Value is :"+element));

        Thread.sleep(1000);

    }

    @Test
    public void infiniteSequenceTest() throws InterruptedException {
        Flux<Long> finateFlux= Flux.interval(Duration.ofMillis(100))
                .take(3)
                .log();
     StepVerifier.create(finateFlux)
             .expectSubscription()
             .expectNext(0L,1L,2L)
             .verifyComplete();

    }
    @Test
    public void infiniteSequenceMapTest() throws InterruptedException {
        Flux<Integer> finateFlux= Flux.interval(Duration.ofMillis(100))
                .map(l->new Integer(l.intValue()))
                .take(3)
                .log();
        StepVerifier.create(finateFlux)
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();

    }

    @Test
    public void infiniteSequenceMapTest_withDelay() throws InterruptedException {
        Flux<Integer> finateFlux= Flux.interval(Duration.ofMillis(100))
                .delayElements(Duration.ofSeconds(1))
                .map(l->new Integer(l.intValue()))
                .take(3)
                .log();
        StepVerifier.create(finateFlux)
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();

    }
}
