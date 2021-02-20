package com.vincenzoracca.webflux.routers;

import com.vincenzoracca.webflux.handlers.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouter {


    @Bean
    public RouterFunction<ServerResponse> findAllRouter(UserHandler userHandler) {
        return route(GET("/users")
                .and(accept(MediaType.APPLICATION_JSON)), userHandler::findAll);
    }

    @Bean
    public RouterFunction<ServerResponse> findById(UserHandler userHandler) {
        return route(GET("/users/{id}")
                .and(accept(MediaType.APPLICATION_JSON)), userHandler::findById);
    }

    @Bean
    public RouterFunction<ServerResponse> save(UserHandler userHandler) {
        return route(POST("/users")
                .or(PUT("/users"))
                .and(accept(MediaType.APPLICATION_JSON)), userHandler::save);
    }

    @Bean
    public RouterFunction<ServerResponse> delete(UserHandler userHandler) {
        return route(DELETE("/users/{id}")
                .and(accept(MediaType.APPLICATION_JSON)), userHandler::delete);
    }


}
