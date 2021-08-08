package com.learning.reactivespring.reactivespring.handler;

import com.learning.reactivespring.reactivespring.document.Item;
import com.learning.reactivespring.reactivespring.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.PublicKey;

import static com.learning.reactivespring.reactivespring.constants.ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ItemHandler {

    @Autowired
    ItemReactiveRepository reactiveRepository;

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAllItems(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(reactiveRepository.findAll(), Item.class);

    }

    public Mono<ServerResponse> getOneItem(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");
        Mono<Item> itemMono = reactiveRepository.findById(id);

        return itemMono.flatMap(item ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(item)))
                .switchIfEmpty(notFound)
                ;
  /*      return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(reactiveRepository.findById(id),Item.class)
                .switchIfEmpty(ServerResponse.notFound().build());*/
    }

    public Mono<ServerResponse> createItem(ServerRequest serverRequest) {
        Mono<Item> itemToBeInserted = serverRequest.bodyToMono(Item.class);
        return itemToBeInserted.flatMap(
                item -> ServerResponse.created(URI.create(ITEM_FUNCTIONAL_END_POINT_V1 + "/" + item.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(reactiveRepository.save(item), Item.class)
        );
    }

    public Mono<ServerResponse> deleteItem(ServerRequest serverRequest) {
        String deleteId = serverRequest.pathVariable("id");
        //  Mono<Item> itemMono = reactiveRepository.findById(deleteId);
        Mono<Void> deleteItem = reactiveRepository.deleteById(deleteId);

       /*return itemMono.flatMap(item ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(reactiveRepository.deleteById(item.getId()), Item.class))
                .switchIfEmpty(ServerResponse.noContent().build());*/

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deleteItem, Void.class);
    }

    public Mono<ServerResponse> updatedItem(ServerRequest serverRequest) {
        String updateItemId = serverRequest.pathVariable("id");
        Mono<Item> updatedItem =
                serverRequest.bodyToMono(Item.class).flatMap(
                item ->
                        {
                            Mono<Item> itemMono = reactiveRepository.findById(updateItemId)
                                    .flatMap(
                                            currentItem -> {
                                                currentItem.setDescription(item.getDescription());
                                                currentItem.setPrice(item.getPrice());
                                                return reactiveRepository.save(currentItem);
                                            });
                            return itemMono;
                        });

        return updatedItem.flatMap(item ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(item))
        )
                .switchIfEmpty(notFound);
    }

}
