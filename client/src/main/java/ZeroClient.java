import javafx.application.Platform;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQException;

public class ZeroClient {

    private static final int REQUEST_TIMEOUT = 1000;
    private static final int MAX_RETRIES     = 3;   //  Before we abandon

    private ZMQ.Context context;
    private ZMQ.Socket socket;
    private ZMQ.Socket displaySocket;
    private ChatController controller;
    private String topic = "[main]";

    ZeroClient(ChatController pmController) {
        controller = pmController;
        init();
    }

    void init() {
        context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Connecting to hello world server");

        socket = context.socket(ZMQ.PUSH);

        displaySocket = context.socket(ZMQ.SUB);
        displaySocket.subscribe(topic.getBytes());

        socket.connect("tcp://*:5555");
        displaySocket.connect("tcp://*:5556");

        startListenerThread();
    }

    void close () {
        System.out.println("Closing connection");
        socket.close();
        context.term();
    }

    void send (String message) {
        System.out.println("Sending message");
        byte[] reply;
        try {
            socket.send(message.getBytes(ZMQ.CHARSET), 0);
            Thread.sleep(100);
        } catch (Exception e) {
            return;
        }

    }

    private void startListenerThread() {
        Thread listenerThread = new Thread(() -> {
            byte[] reply;
            while (true) {
                try {
                    Thread.sleep(100);
                    reply = displaySocket.recv(0);
                    if (reply.length > 0) {
//                        System.out.println("UN:" + displaySocket.getPlainUsername());
                        final String str = new String(reply, ZMQ.CHARSET).replace(topic, "");
                        Platform.runLater(() -> {
                            controller.onSocketMessage(str);
                        });
                    }
                } catch (ZMQException e) {
//                    System.out.println("Listener thread: ZMQExeption" + e.getMessage());
                    continue;
                } catch (Exception e) {
                    System.out.println("Listener thread: Another exeption");
                    break;
                }
            }
        });
        listenerThread.start();
    }

    public Socket getSocket() {
        return socket;
    }

}

