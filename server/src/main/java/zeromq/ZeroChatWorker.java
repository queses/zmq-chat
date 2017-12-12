package zeromq;

import chat.ChatConstants;
import chat.ChatWorker;
import org.zeromq.ZMQ;
import utils.AppEnv;
import utils.PubSub;

import java.util.Optional;

/**
 * Сервер можно сделать мультипоточным:
 * https://github.com/zeromq/jeromq/blob/master/src/test/java/guide/mtserver.java
 */
public class ZeroChatWorker implements ChatWorker {
    private PubSub pubSub;
    private Thread listenThread;
    private ZMQ.Socket socket;
    private ZMQ.Socket displaySocket;
    private ZMQ.Context context;

    public ZeroChatWorker(PubSub pubSub) {
        this.pubSub = pubSub;
    }

    public void init() {
        context = ZMQ.context(1);

        //  Socket to talk to clients
        socket = context.socket(ZMQ.PULL);
        displaySocket = context.socket(ZMQ.PUB);

        String sendAddr = Optional.ofNullable(AppEnv.get("SEND_SOCKET_ADDR")).orElse("tcp://*:5555");
        String displayAddr = Optional.ofNullable(AppEnv.get("DISPLAY_SOCKET_ADDR")).orElse("tcp://*:5556");
        socket.bind(sendAddr);
        displaySocket.bind(displayAddr);

        listenThread = getListenThread();
        listenThread.start();

        System.out.printf("Server started at: %s, %s", sendAddr, displayAddr);
    }

    public void close() {
        System.out.println("Closing conntection.");
        listenThread.interrupt();
        socket.close();
        displaySocket.close();
//        context.term();
    }

    public void sendMessageInTopic(String topic, String message) {
        displaySocket.send(String.format("[%s]%s", topic, message), 0);
    }

    private Thread getListenThread() {
        return new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                byte[] reply = socket.recv(0);
                String str = new String(reply, ZMQ.CHARSET);

                pubSub.publish(ChatConstants.EVENT_MESSAGE_RECV, str);

                try {
                    Thread.sleep(100); //  Do some 'work'
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}
