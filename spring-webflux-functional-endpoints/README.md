# Spring WebFlux with Functional Endpoints
There are two functions used to build endpoints very quickly and handle in asynchronous way.

### Handler Function
A handler function is function which takes the ServerRequest and returns of Mono of ServerResponse. Both the ServerRequest and ServerResponse 
handle in asynchronous way. To convert request body to POJO we use bodyToMono({Class in which to convert}).Then we perform operation and returns 
the Mono of ServerResponse.


### Router Function
A router function is the function which takes the ServerRequest and returns Mono HandlerFunction. We generally don't write Router functions 
we user the static method route from the RouterFunction, where we use RequestPredicate Class to map a handler function with the path and method 
and the handler function handles the request processing.

## Hands-on
For Experimenting this we will try to build below APIs using reactive programming(Mono, Flux) and functional endpoints.

We have a product catalog for which we need to expose below type of APIs
1. Get all products (endpoint : "/products", Method : GET)
2. Get a specific product (endpoint : "/product/{id}", Method : GET)
3. Register a new product (endpoint = "/product", Method : POST)
4. Update a product (endpoint : "/product/{id}", Method : PUT)
5. Delete a product (endpoint : "/product/{id}", Method : DELETE)
6. Delete all products (endpoint : "/products", Method : DELETE)

When we start building application we taken the Reactive Web, Embedded mongo and reactive mongodb
for libraries which gives the reactive support to our application.

1. First we build the Mongo model object (Product.java).
2. Then Build repository of product class using ReactiveMongoRepository which is reactive implementation for mongoDB (ProductRepository.java)
3. Then we initialize some sample data using either CommandLineRunner or ApplicationRunner (SpringWebfluxFunctionalEndpointsApplication.java)
4. Create handler class (ProductHandler.java)
5. Create routes in mail class (SpringWebfluxFunctionalEndpointsApplication.java). Two approaches are their to define route (routes and nest)
