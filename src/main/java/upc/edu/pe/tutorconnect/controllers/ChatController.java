package upc.edu.pe.tutorconnect.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ConcurrentLinkedQueue;

@RestController
public class ChatController {

    private final ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<>();

    @GetMapping("/chat")
    @ResponseBody
    public Flux<String> getMessages() {
        return Flux.fromIterable(messages)
                .delayElements(Duration.ofSeconds(0));
    }

    @PostMapping(value = "/chat", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public Mono<Void> postMessage(@RequestBody String message) {
        messages.add(message);
        return Mono.empty();
    }

}