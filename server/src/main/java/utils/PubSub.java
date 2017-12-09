package utils;

import java.util.*;

public class PubSub {
    private Map<String, List<PubSubHandler>> handlers;

    public PubSub() {
        this.handlers = new HashMap<>();
    }

    @FunctionalInterface
    public interface PubSubHandler {
        void run(Object payload);
    }

    public void subscribe(String topic, PubSubHandler handler) {
        // Если не находит значение по key, то создает новое с помощью callback-а
        Collection<PubSubHandler> handlerList = handlers.computeIfAbsent(topic, key -> new ArrayList<>());
        handlerList.add(handler);
    }

    public void publish(String topic, Object payload) {
        Optional.ofNullable(handlers.get(topic)).ifPresent(list -> list.forEach(handler -> {
              handler.run(payload);
        }));
//        List<PubSubHandler> handlerList = handlers.get(topic);
//        if (handlerList == null) return;
//        for (PubSubHandler handler : handlerList) {
//            handler.run(payload);
//        }
    }
}

