package com.aasaanshiksha.application.webclient;

import com.aasaanshiksha.application.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ProductWebClient {
    private WebClient webClient;

    public ProductWebClient() {
        webClient = WebClient.builder().
                baseUrl("http://localhost:8080/product").build();
    }

    public Mono<ResponseEntity<Product>> createNewProduct() {
        return webClient.post()
                .body(Mono.just(new Product("4", "Product 4", 550.0)), Product.class)
                .exchange()
                .flatMap(o -> o.toEntity(Product.class))
                .doOnSuccess(o ->
                        System.out.println("Create new product" + o.getBody()));
    }

    public Mono<Product> getProduct(String id) {
        return webClient.get().uri("/{id}", id)
                .retrieve().bodyToMono(Product.class).doOnSuccess(o -> System.out.println("Get called" + o));
    }

    public Mono<Product> updateProduct(String id) {
        return webClient.put().uri("/{id}", id)
                .body(Mono.just(new Product(null, "Product update", 25.0)), Product.class)
                .retrieve().bodyToMono(Product.class).doOnSuccess(o -> System.out.println("Update Called" + o));
    }

    public Mono<Void> deleteProduct(String id) {
        return webClient.delete().uri("/{id}", id)
                .retrieve().bodyToMono(Void.class).doOnSuccess(o -> System.out.println("Delete called" + id));
    }

}
