package com.aasaanshiksha.application;

import com.aasaanshiksha.application.webclient.EventWebClient;
import com.aasaanshiksha.application.webclient.ProductWebClient;
import com.aasaanshiksha.application.webclient.ProductsWebClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringWebFluxClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebFluxClientApplication.class);
        ProductsWebClient productsWebClient = new ProductsWebClient();
        ProductWebClient productWebClient = new ProductWebClient();
        EventWebClient eventWebClient = new EventWebClient();
        productWebClient.createNewProduct()
                .thenMany(productsWebClient.getAllProducts())
                .take(1)
                .flatMap(p -> productWebClient.updateProduct(p.getId()))
                .flatMap(p -> productWebClient.getProduct(p.getId()))
                .flatMap(p -> productWebClient.deleteProduct(p.getId()))
                .thenMany(productsWebClient.getAllProducts())
                .thenMany(eventWebClient.getEvents())
                .subscribe(System.out::println);
        //Thread.sleep(5000);
    }
}
