# Spring Webflux Client
Till now we had exposed API using annotated controllers or functional endpoints using the Spring webflux
in reactive manner. In this module, We are going to consume these API using spring webflux client.

For our synchronous operation in the Spring MVC we had RestTemplate which consumes the API. Now to consume
the API response we use the WebClient which native support is provided by the Reactor and Netty.

WebClient is an Interface which comes with some default methods like create() and build().
```java
WebClient webclient = WebClient.create(); /*Creates a simple web client for calling the 
API we need to provide full path*/

WebClient webClientWithBaseURL = WebClient.create("http://localhost:8080");/*Use base path as provided and 
append the request path after it*/

WebClient webClientWithBaseURLAndHeader = WebClient.builder()
                                            .baseUrl("http://localhost:8080")
                                            .defaultHeader(HttpHeaders.USER_AGENT,"Application A")
                                            .build();
```

The above code block shows the methods to create WebClient, In above block the property we initialized are fix and
can not be changed. If we want to create webclient with same properties and modify some of them then we need to use
mutate method of this class.

Let's Now see how to consume the request using WebClient.

When we use GET or DELETE method we use the below format
1. We call the method using the WebClient
2. Pass the URI with the parameters as arguments
3. Set the header specs
4. Call retrieve to get the data
5. Get the data using bodyToMono or Flux (Optionally you can check status and perform this) 

When we use the POST or PUT method we use the below format
1. We call the method using the Webclient
2. Pass the URI with the parameters as arguments
3. Set content type and header
4. Pass the body and class parameter in method
5. Call retrieve to get the data
6. Get the data using bodyToMono or Flux (Optionally you can check status and perform this) 

We have also exchange method to retrieve response from server.

If we are consuming synchronous API then we can use syncMethod. 

If we are consuming MutiPart request then we use BodyInserters.fromMultiPartData(data).with("file",file).

# Hands On explanation
1. Copy model objects to project directory.
2. Now we create WebClients for APIs.
   <br/>There are two ways to create WebClient.
   <br/>a.
   ```java
    WebClient webClient = WebClient.create("http://localhost:8080/event");
   ```
   <br/>b.
      ```java
       WebClientwebClient = WebClient.builder()
                                .baseUrl("http://localhost:8080/product").build();
      ```
3. For calling API we use below code:
    <br/>i. GET or DELETE
    ```java
       return webClient.get().uri("/{id}", id)
                           .retrieve().bodyToMono(Product.class).doOnSuccess(o -> System.out.println("Get called"+o));
    ```
 
    ii. POST or PUT
    ```java
      return webClient.post()
                      .body(Mono.just(new Product("4", "Product 4", 550.0)), Product.class)
                      .exchange()
                      .flatMap(o -> o.toEntity(Product.class))
                      .doOnSuccess(o ->
                              System.out.println("Create new product" + o.getBody()));
    ```
    or
    ```java
            return webClient.put().uri("/{id}", id)
                            .body(Mono.just(new Product(null, "Product update", 25.0)), Product.class)
                            .retrieve().bodyToMono(Product.class).doOnSuccess(o -> System.out.println("Update Called"+o));
    ```
   
   We have two methods to calling API retrieve and exchange.
   
#Common Gotchas
We had initially use main method directly without Spring Boot, then We have to put Thread.sleep(5000) in main method,
because when main thread exit the request processing threads also stopped.
<br/>Also after putting sleep in main method the event request will not work and throw illegal argument exception, because 
we are running the application in standalone mode and event processing require multithreaded environment, for which we 
initialized application as Spring Boot Application.  