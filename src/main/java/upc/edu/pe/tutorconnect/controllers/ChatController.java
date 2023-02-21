package upc.edu.pe.tutorconnect.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
public class ChatController {
    ConcurrentHashMap<Integer, List<String>> messages = new ConcurrentHashMap<>();
    @GetMapping("/chat")
    @ResponseBody
    public Flux<String> getMessages(@RequestParam("chatId") Integer chatId) {
        if (messages.containsKey(chatId)) {
            return Flux.fromIterable(messages.get(chatId))
                    .delayElements(Duration.ofSeconds(0));
        }

        return Flux.empty();
    }

    @PostMapping(value = "/chat", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public Mono<Void> postMessage(@RequestBody String message, @RequestParam("chatId") Integer chatId) {

        if (messages.containsKey(chatId)) {
            messages.get(chatId).add(message);
        } else {
            messages.put(chatId, new ArrayList<>());
            messages.get(chatId).add(message);
        }

        return Mono.empty();
    }

}
