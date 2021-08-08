package com.learning.reactivespring.reactivespring.fluxamdmonoplayground;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

//Back pressure is the Subscriber will have the access to control the publisher
public class FluxAndMonoBackPressureTest {
    @Test
    public void backPressureTest(){
       Flux<Integer> finateFlux= Flux.range(1,10);

        StepVerifier.create(finateFlux.log())
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();

    }

    @Test
    public void backPressure(){
        Flux<Integer> finateFlux= Flux.range(1,10);

      finateFlux.subscribe(
              element -> System.out.println("Element is " +element),
              e-> System.out.println("Exception is: "+e),
              ()-> System.out.println("completed"),
              subscription -> subscription.request(2));

    }

    @Test
    public void backPressure_cancel(){
        Flux<Integer> finateFlux= Flux.range(1,10);

        finateFlux.subscribe(
                element -> System.out.println("Element is " +element),
                e-> System.out.println("Exception is: "+e),
                ()-> System.out.println("completed"),
                subscription -> subscription.cancel());

    }

    @Test
    public void customized_backPressure(){
        Flux<Integer> finateFlux= Flux.range(1,10).log();

        finateFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                super.hookOnNext(value);
                request(1);
                System.out.println(" Value is: "+value);
                if(value==4){
                    cancel();
                }
            }
        });

    }

}
