package chat;

import utils.PubSub;

public class ChatPubSub extends PubSub {
    private static ChatPubSub ourInstance = new ChatPubSub();

    public static ChatPubSub getInstance() {
        return ourInstance;
    }

    private ChatPubSub() {
    }
}
