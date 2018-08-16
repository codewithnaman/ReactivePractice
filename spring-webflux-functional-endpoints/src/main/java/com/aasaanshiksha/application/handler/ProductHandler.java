package com.aasaanshiksha.application.handler;

import com.aasaanshiksha.application.model.Event;
import com.aasaanshiksha.application.model.Product;
import com.aasaanshiksha.application.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ProductHandler {

    @Autowired
    private ProductRepository productRepository;

    public Mono<ServerResponse> getAllProducts(ServerRequest serverRequest) {
        Flux<Product> products = productRepository.findAll();

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(products, Product.class);
    }

    public Mono<ServerResponse> getProduct(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Product> productMono = productRepository.findById(id);
        Mono<ServerResponse> notFoundReponse = ServerResponse.notFound().build();

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(productMono, Product.class)
                .switchIfEmpty(notFoundReponse);
        //DefaultIfEmpty works when we are dealing with object while the switchIf empty works
        //for subscribers or stream
    }

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        Mono<Product> newProduct = serverRequest.bodyToMono(Product.class);

        return newProduct.flatMap(product -> ServerResponse.status(HttpStatus.CREATED).
                contentType(MediaType.APPLICATION_JSON)
                .body(productRepository.save(product), Product.class));
    }

    public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Product> productInfomation = serverRequest.bodyToMono(Product.class);
        Mono<Product> existingProduct = productRepository.findById(id);
        Mono<ServerResponse> notFoundReponse = ServerResponse.notFound().build();

        return Mono.zip(productInfomation, existingProduct, (updateProduct, product) ->
                new Product(product.getId(), updateProduct.getName(), updateProduct.getPrice())).flatMap(
                product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(productRepository.save(product), Product.class)
        ).switchIfEmpty(notFoundReponse);
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Product> existingProduct = productRepository.findById(id);
        Mono<ServerResponse> notFoundResponse = ServerResponse.notFound().build();

        return existingProduct.flatMap(product ->
                ServerResponse.ok().build(productRepository.delete(product)))
                .switchIfEmpty(notFoundResponse);
    }

    public Mono<ServerResponse> deleteAllProducts(ServerRequest serverRequest) {
        return ServerResponse.ok().build(
                productRepository.deleteAll()
        );
    }

    public Mono<ServerResponse> generateEvents(ServerRequest serverRequest) {
        Flux<Event> events = Flux.interval(Duration.ofSeconds(1)).map(
                id -> new Event(id, "test")
        );

        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(events, Event.class);
    }
}
