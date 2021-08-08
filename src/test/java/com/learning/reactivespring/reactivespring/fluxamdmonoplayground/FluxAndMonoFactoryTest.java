package com.learning.reactivespring.reactivespring.fluxamdmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

// Different ways of creating mono and Flux
public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    //fromIterable
    @Test
    public void fluxUsingIterable() {
        Flux<String> stringFlux = Flux.fromIterable(names);
        StepVerifier.create(stringFlux.log())
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    //fromArray
    @Test
    public void fluxUsingArray() {
        String[] names = {"adam", "anna", "jack", "jenny"};
        Flux<String> stringFlux = Flux.fromArray(names);
        StepVerifier.create(stringFlux.log())
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    //fromStream
    @Test
    public void fluxUsingStream() {
        Flux<String> stringFlux = Flux.fromStream(names.stream());
        StepVerifier.create(stringFlux.log())
                .expectNextCount(4)
           //     .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    //Mono Factory Methods
    @Test
    public void monoUsingJustOrEmpty(){
        Mono<Object> objectMono = Mono.justOrEmpty(null);

        StepVerifier.create(objectMono.log())
                .verifyComplete();
    }

    //fromSupplier
    @Test public void monoUsingSupplier(){
        Supplier<String> sup= () -> "adam";
        Mono<String> stringMono = Mono.fromSupplier(() -> "adam");
        StepVerifier.create(stringMono.log())
                .expectNext("adam")
                .verifyComplete();

    }

    //range
    @Test
    public void fluxUsingRange(){
        Flux<Integer> range = Flux.range(1, 5);
        StepVerifier.create(range.log()).expectNext(1,2,3,4,5).verifyComplete();
    }
}
