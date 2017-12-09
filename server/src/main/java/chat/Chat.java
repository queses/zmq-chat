package chat;

import utils.PubSub;

public class Chat {
    private PubSub pubSub;
    private ChatWorker worker;

    public Chat(PubSub pubSub, ChatWorker worker) {
        this.pubSub = pubSub;
        this.worker = worker;
        subscribe();
    }

    private void subscribe () {
        pubSub.subscribe(ChatConstants.EVENT_MESSAGE_RECV, payload -> {
            System.out.println("Received " + payload);
            worker.sendMessageInTopic(ChatConstants.MAIN_TOPIC, payload.toString());
        });
    }

}
