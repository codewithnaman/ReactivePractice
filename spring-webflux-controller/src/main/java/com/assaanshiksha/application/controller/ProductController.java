package com.assaanshiksha.application.controller;

import com.assaanshiksha.application.model.Event;
import com.assaanshiksha.application.model.Product;
import com.assaanshiksha.application.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/product/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable("id") String id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok().body(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/product")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> saveProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/product/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@RequestBody Product product,
                                                       @PathVariable("id") String id) {
        return productRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    return productRepository.save(existingProduct);
                })
                .map(updateProduct -> ResponseEntity.ok(updateProduct))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/product/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable("id") String id) {
        return productRepository.findById(id)
                .flatMap(exisitingProduct -> productRepository.delete(exisitingProduct).then(
                        Mono.just(ResponseEntity.ok().<Void>build())
                )).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/products")
    public Mono<Void> deleteAllProducts() {
        return productRepository.deleteAll();
    }

    @GetMapping(value = "/event", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Event> publishEvent() {
        return Flux.interval(Duration.ofSeconds(1)).map(
                input -> new Event(input, "Test")
        );
    }
}
