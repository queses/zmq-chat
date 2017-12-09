package chat;

public interface ChatWorker {
    void init();
    void close();
    void sendMessageInTopic(String topic, String message);
}
