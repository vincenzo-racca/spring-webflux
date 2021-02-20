package com.vincenzoracca.webflux.resources;

import com.vincenzoracca.webflux.domains.User;
import com.vincenzoracca.webflux.repos.UserMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserMongoRepository userMongoRepository;

    @GetMapping
    public Mono<ResponseEntity<List<User>>> findAll() {
        return userMongoRepository.findAll()
                .collectList()
                .map(users -> {
                    if(users.isEmpty()) {
                        return ResponseEntity.noContent().build();
                    }
                    return ResponseEntity.ok(users);
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> findById(@PathVariable String id) {
        return userMongoRepository.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

   @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public Mono<ResponseEntity<User>> save(@Valid @RequestBody User user) {
        return userMongoRepository.save(user)
                .map(userSaved -> UriComponentsBuilder.fromPath(("/{id}")).buildAndExpand(userSaved.getId()).toUri())
                .map(uri -> ResponseEntity.created(uri).build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void save(@PathVariable String id) {
//        userMongoRepository.deleteById(id); //non funziona perchè non fa parte di una chain, quindi non è sottoscritto da nessun metodo
        //nulla succede se non ti sottoscrive
        userMongoRepository.deleteById(id).subscribe();
    }
}
