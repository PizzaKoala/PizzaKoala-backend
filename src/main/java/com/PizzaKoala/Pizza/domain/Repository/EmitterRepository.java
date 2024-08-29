package com.PizzaKoala.Pizza.domain.Repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class EmitterRepository {
    private final Map<String, SseEmitter> emitterMap = new HashMap<>();

    public SseEmitter save(Long memberId, SseEmitter sseEmitter) {
        final String key = getKey(memberId);
        emitterMap.put(key, sseEmitter);
        log.info("Set sseEmitter {}", memberId);
        return sseEmitter;
    }
    public Optional<SseEmitter> get(Long memberId) {
        final String key = getKey(memberId);
        log.info("Get sseEmitter {}", memberId);
        return Optional.ofNullable(emitterMap.get(key));
    }

    public void delete(Long memberId) {
        emitterMap.remove(getKey(memberId));
    }

    private String getKey(Long memberId) {
        return "Emitter:UID:" + memberId;
    }
}
