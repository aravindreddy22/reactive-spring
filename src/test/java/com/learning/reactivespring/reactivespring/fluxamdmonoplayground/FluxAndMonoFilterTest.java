package com.learning.reactivespring.reactivespring.fluxamdmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {


    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void filterTest() {
        Flux<String> filterFlux = Flux.fromIterable(names).filter(s -> s.startsWith("a")).log();
        StepVerifier.create(filterFlux).expectNext("adam", "anna").verifyComplete();
    }

    @Test
    public void filterTest_length() {
        Flux<String> filterFlux = Flux.fromIterable(names).filter(s -> s.length()>4).log();
        StepVerifier.create(filterFlux).expectNext("jenny").verifyComplete();
    }
}
