package chat;

public interface ChatWorker {
    void init();
    void close();
    void sendMessage(String message);
}
