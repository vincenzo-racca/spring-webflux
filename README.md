## A Spring WebFlux Project with Mongo Reactive

This project consists in two Maven submodules.\
The "functional" module, contains a Web Service REST with Spring WebFlux with functional endpoints (RouterFunctions). \
The "traditional" module, contains a Web Service REST with Spring WebFlux with annotated endpoints (@RestController, etc..). 

Both projects do CRUD operations with ReactiveMongo, they use an embedded MongoDB. \

You can test the endpoints by running the SpringWebFluxApplicationTests test class in the functional module. \
It uses WebTestClient class, the test class for WebClient, the new Spring REST Client not blocking.

#### Running the project:

The project contains a mvnw file (for both submodules), it's a Maven wrapper, you don't need to have maven installed
to launch the application. \
So, you can run the project immediately.

Go to functional or traditional submodule, and run this command:

Windows: `mvnw.cmd spring-boot:run`

Unix: `./mvnw spring-boot:run`

Then, your endpoints are available on: `localhost:8080`