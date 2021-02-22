package com.vincenzoracca.webflux;

import com.vincenzoracca.webflux.domains.User;
import com.vincenzoracca.webflux.handlers.UserHandler;
import com.vincenzoracca.webflux.repos.UserMongoRepository;
import com.vincenzoracca.webflux.routers.UserRouter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;


@SpringBootTest
class SpringWebFluxApplicationTests {

    @Autowired
    private UserRouter userRouter;

    @Autowired
    private UserHandler userHandler;

    @MockBean
    private UserMongoRepository userMongoRepository;

    @Test
    public void findAllTest() {

        WebTestClient client = WebTestClient
                .bindToRouterFunction(userRouter.findAllRouter(userHandler))
                .build();

        List<User> users = Arrays.asList(new User("Mario","Rossi"),
                new User("Filippo", "Bianchi"));

        Flux<User> flux = Flux.fromIterable(users);
        given(userMongoRepository.findAll())
                .willReturn(flux);

        client.get().uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .isEqualTo(users);
    }

    @Test
    public void findByIdTest() {

        WebTestClient client = WebTestClient
                .bindToRouterFunction(userRouter.findById(userHandler))
                .build();

        User user = new User("Bobo","Vieri");
        user.setId("efgt-fght");

        Mono<User> mono = Mono.just(user);
        given(userMongoRepository.findById("efgt-fght"))
                .willReturn(mono);

        client.get().uri("/users/efgt-fght")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .isEqualTo(user);
    }

    @Test
    public void saveTest() {

        WebTestClient client = WebTestClient
                .bindToRouterFunction(userRouter.save(userHandler))
                .build();

        User user = new User("Bobo","Vieri");
        user.setId("efgt-fght");

        Mono<User> mono = Mono.just(user);
        given(userMongoRepository.save(user))
                .willReturn(mono);

        client.post().uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .body(mono, User.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader()
                .location("/efgt-fght");
    }



}
