package com.aasaanshiksha.application.webclient;

import com.aasaanshiksha.application.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductsWebClient {

    private WebClient webClient;

    public ProductsWebClient() {
        webClient = WebClient.create("http://localhost:8080/products");
    }

    public Flux<Product> getAllProducts() {
        return webClient.get().retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(o -> System.out.println("Get all products " + o));
    }

    public Mono<ResponseEntity<Void>> deleteAllProducts() {
        return webClient.delete().exchange()
                .flatMap(response -> response.toEntity(Void.class))
                .doOnSuccess(o -> System.out.println("Delete all products " + o));
    }
}
