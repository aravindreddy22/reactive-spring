package com.learning.reactivespring.reactivespring.fluxamdmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void transformUsingMap(){
        Flux<String> namesFlux = Flux.fromIterable(names)
                .map(String::toUpperCase)
                .log();
        StepVerifier.create(namesFlux).expectNext("ADAM","ANNA","JACK","JENNY").verifyComplete();
    }


    @Test
    public void transformUsingMap_Length(){
        Flux<Integer> namesFlux = Flux.fromIterable(names)
                .map(String::length)
                .log();
        StepVerifier.create(namesFlux).expectNext(4,4,4,5).verifyComplete();
    }

    @Test
    public void transformUsingMap_Length_Repeat(){
        Flux<Integer> namesFlux = Flux.fromIterable(names)
                .map(String::length)
                .repeat(1)
                .log();
        StepVerifier.create(namesFlux).expectNext(4,4,4,5,4,4,4,5).verifyComplete();
    }

    @Test
    public void transformUsingMap_Length_Filter(){
        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s-> s.length()>4)
                .map(String::toUpperCase)
                .log();
        StepVerifier.create(namesFlux).expectNext("JENNY").verifyComplete();
    }

    @Test
    public void transformFlatMap(){
        Flux<String> names= Flux.fromIterable(Arrays.asList("A","B","C","D","E"))
                .flatMap(s-> Flux.fromIterable(convertToList(s)))
                .log(); //db call or external service call returns flux in this case we use flatmap.

       StepVerifier.create(names)
               .expectNextCount(10)
               .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s,"newValue");
    }

    @Test
    public void transformFlatMap_parallel(){
        Flux<String> names= Flux.fromIterable(Arrays.asList("A","B","C","D","E"))
                .window(2)
                .flatMap(s->s.map(this::convertToList).subscribeOn(parallel()))
                .flatMap(s->Flux.fromIterable(s))
                .log(); //db call or external service call returns flux in this case we use flatmap.

        StepVerifier.create(names)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void transformFlatMap_parallel_seq(){
        Flux<String> names= Flux.fromIterable(Arrays.asList("A","B","C","D","E"))
                .window(2)
             // .concatMap(s->s.map(this::convertToList).subscribeOn(parallel()))
                .flatMapSequential(s->s.map(this::convertToList).subscribeOn(parallel()))
                .flatMap(s->Flux.fromIterable(s))
                .log(); //db call or external service call returns flux in this case we use flatmap.

        StepVerifier.create(names)
                .expectNextCount(10)
                .verifyComplete();
    }
}
