package com.aasaanshiksha.application;

import com.aasaanshiksha.application.handler.ProductHandler;
import com.aasaanshiksha.application.model.Product;
import com.aasaanshiksha.application.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Flux;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@SpringBootApplication
public class SpringWebfluxFunctionalEndpointsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebfluxFunctionalEndpointsApplication.class, args);
    }

    @Bean
    public CommandLineRunner insertSampleData(ProductRepository productRepository) {
        return args -> {
            Flux<Product> productFlux = Flux.just(
                    new Product("1", "Product 1", 100.00),
                    new Product("2", "Product 2", 20.00),
                    new Product("3", "Product 3", 500.00)
            ).flatMap(productRepository::save);

            productFlux.thenMany(productRepository.findAll()).subscribe(System.out::println);
        };
    }

    @Bean
    public RouterFunction buildRoutes(ProductHandler productHandler) {
   /*     return RouterFunctions.route(GET("/products"),productHandler::getAllProducts)
                .andRoute(GET("/product/{id}"),productHandler::getProduct)
                .andRoute(POST("/product"),productHandler::createProduct)
                .andRoute(PUT("/product/{id}"),productHandler::updateProduct)
                .andRoute(DELETE("/product/{id}"),productHandler::deleteProduct)
                .andRoute(DELETE("/products"),productHandler::deleteAllProducts)
                .andRoute(GET("/event"),productHandler::generateEvents)*/
        ;
        return RouterFunctions.nest(path("/products"),
                RouterFunctions.route(GET("/"), productHandler::getAllProducts).
                        andRoute(DELETE("/"), productHandler::deleteAllProducts)
        ).andNest(path("/product"),
                RouterFunctions.route(POST("/"), productHandler::createProduct)
                        .andNest(path("/{id}"),
                                RouterFunctions.route(GET("/"), productHandler::getProduct)
                                        .andRoute(PUT("/"), productHandler::updateProduct)
                                        .andRoute(DELETE("/"), productHandler::deleteProduct))
        ).andNest(path("/event"), RouterFunctions.route(GET("/"), productHandler::generateEvents));
    }
}
