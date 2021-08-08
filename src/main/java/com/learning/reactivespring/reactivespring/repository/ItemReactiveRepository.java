package com.learning.reactivespring.reactivespring.repository;

import com.learning.reactivespring.reactivespring.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ItemReactiveRepository  extends ReactiveMongoRepository<Item,String> {
     Mono<Item> findByDescription(String descripton);

}
