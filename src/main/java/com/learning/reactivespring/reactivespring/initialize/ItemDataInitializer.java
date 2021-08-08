package com.learning.reactivespring.reactivespring.initialize;

import com.learning.reactivespring.reactivespring.document.Item;
import com.learning.reactivespring.reactivespring.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Override
    public void run(String... args) throws Exception {
        initialDataSetUp();
    }

    private void initialDataSetUp() {
        List<Item> itemsList = Arrays.asList(
                new Item(null, "Samsung Tv", 400.00),
                new Item(null, "LG Tv", 420.00),
                new Item(null, "Apple Watch", 440.00),
                new Item("ABC", "Beats", 149.99));

        itemReactiveRepository.deleteAll().
                thenMany(Flux.fromIterable(itemsList).log())
                .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe(item -> System.out.println("item inserted from command line runner "+item.getDescription()));

    }
}
