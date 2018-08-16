# Spring webflux with controller
The learning curve for spring webflux is less if you know the spring MVC already.
For developing the spring webflux APIs we can user our existing annotation
like @Controller, @RestController etc. for defining the endpoints.


The main difference between Spring webflux and Spring MVC applications are the how
the request and response are handled and the support of reactive types.
ServerWebExchange acts like request and response at low level, but for more of them we
are going to use ServerHttpRequest and ServerHttpResponse, reactive version of request
response. Reactive types also have below support as well
    1. WebSession
    2. java.security.Principle
    3. Argument annotated with @RequestBody
    4. HttpEntity
    5. In Multi-part data @RequestPart
    

## Hands On exercise 
For Experimenting this we will try to build below APIs using reactive programming(Mono, Flux).

We have a product catalog for which we need to expose below type of APIs
1. Get all products (endpoint : "/products", Method : GET)
2. Get a specific product (endpoint : "/product/{id}", Method : GET)
3. Register a new product (endpoint = "/product", Method : POST)
4. Update a product (endpoint : "/product/{id}", Method : PUT)
5. Delete a product (endpoint : "/product/{id}", Method : DELETE)
6. Delete all products (endpoint : "/products", Method : DELETE)
7. Events which published in every 1 secs. (endpoint : "/event", Method : GET) <b>Not able to see result on any browser other than Chrome</b>

When we start building application we taken the Reactive Web, Embedded mongo and reactive mongodb
for libraries which gives the reactive support to our application.

1. First we build the Mongo model object (Product.java).
2. Then Build repository of product class using ReactiveMongoRepository which is reactive implementation for mongoDB (ProductRepository.java)
3. Then we initialize some sample data using either CommandLineRunner or ApplicationRunner (SpringWebFluxAnnotationControllerApplication.java)<br/>
   Ref: https://dzone.com/articles/spring-boot-applicationrunner-and-commandlinerunne
4. Then we build endpoints and write logic to convert and write Mono and Flux (ProductController.java)