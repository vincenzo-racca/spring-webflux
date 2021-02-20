package com.vincenzoracca.webflux.handlers;

import com.vincenzoracca.webflux.domains.User;
import com.vincenzoracca.webflux.repos.UserMongoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class UserHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(UserHandler.class);


    private final UserMongoRepository userMongoRepository;

    private final ValidatorHandler validatorHandler;


    public Mono<ServerResponse> findAll(ServerRequest request) {
        return userMongoRepository.findAll()
                .collectList()
                .flatMap(users -> {
                    if(users.isEmpty()) {
                        return ServerResponse.noContent().build();
                    }
                    return  ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(users));
                });
    }


    public Mono<ServerResponse> findById(ServerRequest request) {
        return userMongoRepository.findById(request.pathVariable("id"))
                .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(user)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }


    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(User.class)
                .doOnNext(validatorHandler::validate)
                .flatMap(userMongoRepository::save)
                .doOnSuccess(userSaved -> LOGGER.info("User saved with id: " +userSaved.getId()))
                .doOnError(e -> LOGGER.error("Error in saveUser method", e))
                .map(userSaved -> UriComponentsBuilder.fromPath(("/{id}")).buildAndExpand(userSaved.getId()).toUri())
                .flatMap(uri -> ServerResponse.created(uri).build());
//                .onErrorResume(throwable -> {
//                    if(throwable instanceof ValidationException) {
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage());
//                    }
//                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage());
//                });
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return userMongoRepository.deleteById(request.pathVariable("id"))
                .then(ServerResponse.noContent().build());
    }

}
