package com.learning.reactivespring.reactivespring.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
public class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void flux_approach1(){
        Flux<Integer> integerFlux = webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();
        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .verifyComplete();
    }

    @Test
    public void flux_approach2(){
        webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .hasSize(4);

    }

    @Test
    public void flux_approach3(){
        List<Integer> expectedList= Arrays.asList(1,2,3,4);
        EntityExchangeResult<List<Integer>> listEntityExchangeResult = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();
        assertEquals(expectedList,listEntityExchangeResult.getResponseBody());
    }

    @Test
    public void flux_approach4(){
        List<Integer> expectedList= Arrays.asList(1,2,3,4);
        webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith(response1-> assertEquals(expectedList, response1.getResponseBody())
                );
    }

    @Test
    public void flux_approach5(){
        Flux<Long> responseBody = webTestClient.get().uri("/fluxstream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(0l)
                .expectNext(1l)
                .expectNext(2l)
                .thenCancel()
                .verify();
    }

    @Test
    public void monoTest(){
        webTestClient.get().uri("/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(response ->
                        assertEquals(1,response.getResponseBody()));
    }

}
