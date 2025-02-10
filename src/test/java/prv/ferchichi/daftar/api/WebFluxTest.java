package prv.ferchichi.daftar.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.test.StepVerifier;

import java.time.Duration;

@ExtendWith(MockitoExtension.class)
public class WebFluxTest {
    @Test
    void shouldSlowdownOnlyEvens() {
        final Flux<Integer> globalFlux = Flux.range(1, 1000)
                .delayElements(Duration.ofMillis(100))
                .groupBy(number -> number % 2 == 0)
                .flatMap(this::dispatch);

        StepVerifier.create(globalFlux)
                .expectNextCount(1000)
                .verifyComplete();
    }

    private Publisher<? extends Integer> dispatch(GroupedFlux<Boolean, Integer> groupedFlux) {
        return groupedFlux.key() ?
                // even flux
                groupedFlux
                        .buffer(Duration.ofMillis(1000))
                        .flatMapIterable(list -> {
                            list.forEach(System.out::println);
                            return list;
                        })
                        .doOnNext(System.out::println) :
                groupedFlux.doOnNext(System.out::println);
    }
}
