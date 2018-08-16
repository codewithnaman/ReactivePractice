package com.assaanshiksha.application;

import com.assaanshiksha.application.model.Product;
import com.assaanshiksha.application.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringWebFluxAnnotationControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebFluxAnnotationControllerApplication.class, args);
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
}
