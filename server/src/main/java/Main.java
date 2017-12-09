import chat.Chat;
import utils.PubSub;
import zeromq.ZeroChatWorker;

public class Main {
    public static void main(String[] args) throws Exception
    {
        PubSub pubSub = new PubSub();
        ZeroChatWorker worker = new ZeroChatWorker(pubSub);
        Chat chat = new Chat(pubSub, worker);
        worker.init();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Closing application.");
            worker.close();
        }));
    }

}
