package com.learning.reactivespring.reactivespring.fluxamdmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;
import reactor.core.Exceptions.*;

import java.time.Duration;

public class FluxAndMonoErrorTest {
    @Test
    public void fluxErrorHandling(){
        Flux<String> stringFlux= Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred!!!")))
                .concatWith(Flux.just("D"))
                .onErrorResume(e-> {
                    System.out.println(e);
                    return Flux.just("Default","Default1");
                });

        StepVerifier.create(stringFlux.log()).expectNext("A","B","C")
                                       /* .expectError(RuntimeException.class)
                                         .verify();*/
                                        .expectNext("Default","Default1")
                                        .verifyComplete();

    }

    @Test
    public void fluxErrorHandling_onErrorReturn(){
        Flux<String> stringFlux= Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred!!!")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("Default");

        StepVerifier.create(stringFlux.log()).expectNext("A","B","C")
                .expectNext("Default")
                .verifyComplete();

    }

    @Test
    public void fluxErrorHandling_onErrorMap(){
        Flux<String> stringFlux= Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred!!!")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e->new CustomException(e));

        StepVerifier.create(stringFlux.log())
                .expectNext("A","B","C")
                .expectError(CustomException.class)
                .verify();

    }


    @Test
    public void fluxErrorHandling_onErrorRetry(){
        Flux<String> stringFlux= Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred!!!")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e->new CustomException(e))
                .retry(2);

        StepVerifier.create(stringFlux.log())
                .expectNext("A","B","C")
                .expectNext("A","B","C")
                .expectNext("A","B","C")
                .expectError(CustomException.class)
                .verify();

    }


    @Test
    public void fluxErrorHandling_onErrorRetry_Delay(){
        Flux<String> stringFlux= Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred!!!")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e->new CustomException(e))
                .retryWhen(Retry.backoff(2,Duration.ofSeconds(5)));

        StepVerifier.create(stringFlux.log())
                .expectNext("A","B","C")
                .expectNext("A","B","C")
                .expectNext("A","B","C")
                .expectError(Exception.class)
                .verify();

    }


}
