package com.learning.reactivespring.reactivespring.repository;

import com.learning.reactivespring.reactivespring.document.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> itemList= Arrays.asList(
            new Item(null,"Samsung Tv",400.00),
            new Item(null,"LG Tv",420.00),
            new Item(null,"Apple Watch",440.00),
            new Item("ABC","Beats",149.99)
    );
    @BeforeEach
    public  void setUp(){
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("Inserted item is:"+item))
                .blockLast();

    }

    @Test
    public void getAllItems(){
        Flux<Item> all = itemReactiveRepository.findAll();

        StepVerifier.create(all)
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void getItemByID(){
        StepVerifier.create(itemReactiveRepository.findById("ABC"))
        .expectSubscription()
        .expectNextMatches(item->item.getDescription().equalsIgnoreCase("Beats"))
        .verifyComplete();
    }

    @Test
    public void findItemByDescription(){
        StepVerifier.create(itemReactiveRepository.findByDescription("Apple Watch").log("Find By Description: "))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItem(){
        StepVerifier.create(itemReactiveRepository.save(new Item(null,"Google Mini",30.0)).log("Save Item:"))
                .expectSubscription()
                .expectNextMatches(item ->item.getId()!=null && item.getDescription().equalsIgnoreCase("google Mini"))
                .verifyComplete();
    }

    @Test
    public void updateItem(){
        double newprice=500;
        Mono<Item> lg_tv = itemReactiveRepository.findByDescription("LG Tv")
                .map(item -> {
                    item.setPrice(newprice);
                    return item;
                })
                .flatMap(item -> itemReactiveRepository.save(item));


        StepVerifier.create(lg_tv)
                .expectSubscription()
                .expectNextMatches(item->item.getPrice()==500)
                .verifyComplete();
    }

    @Test
    public void deleteItemById(){
        Mono<Void> deletedItem = itemReactiveRepository.findById("ABC")
                .map(item -> item.getId())
                .flatMap(id -> itemReactiveRepository.deleteById(id));

        StepVerifier.create(deletedItem.log("Deleted item: "))
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();

    }

    @Test
    public void deleteItemByItem(){
        Mono<Void> deletedItem = itemReactiveRepository.findByDescription("Beats")
                .map(item -> item.getId())
                .flatMap(id -> itemReactiveRepository.deleteById(id));

        StepVerifier.create(deletedItem.log("Deleted item: "))
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll().log("The new Item List: "))
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();

    }


}
