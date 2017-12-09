package zero;

import chat.ChatConstants;
import chat.ChatController;
import chat.ChatPubSub;
import chat.ChatWorker;
import javafx.application.Platform;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQException;
import utils.AppEnv;

import java.util.Optional;

public class ZeroClient implements ChatWorker {

    private static final int REQUEST_TIMEOUT = 1000;
    private static final int MAX_RETRIES     = 3;   //  Before we abandon

    private Context context;
    private Socket socket;
    private Socket displaySocket;
    private Thread listenerThread;

    public ZeroClient() {
        subscribe();
    }

    public void close () {
        System.out.println("Closing connection");
        socket.close();
        context.term();
    }

    public void sendMessage(String message) {
        System.out.println("Sending chat message.");
        byte[] reply;
        try {
            socket.send(message.getBytes(ZMQ.CHARSET), 0);
            Thread.sleep(100);
        } catch (Exception err) {
            return;
        }
    }

    public void init() {
        context = ZMQ.context(1);

        socket = context.socket(ZMQ.PUSH);
        displaySocket = context.socket(ZMQ.SUB);
        displaySocket.subscribe(String.format("[%s]", ChatConstants.MAIN_TOPIC).getBytes());

        socket.connect(Optional.ofNullable(AppEnv.get("SEND_SOCKET_ADDR")).orElse("tcp://*:5555"));
        displaySocket.connect(Optional.ofNullable(AppEnv.get("SEND_SOCKET_ADDR")).orElse("tcp://*:5556"));

        listenerThread = getListenerThread();
        listenerThread.start();
    }

    private void subscribe() {
        ChatPubSub.getInstance().subscribe(ChatConstants.EVENT_CLOSE, (payload) -> this.close());

        ChatPubSub.getInstance().subscribe(ChatConstants.EVENT_MESSAGE_SENT, (payload) ->
                this.sendMessage(payload.toString()));
    }

    private Thread getListenerThread() {
        return new Thread(() -> {
            byte[] reply;
            while (true) {
                try {
                    Thread.sleep(100);
                    reply = displaySocket.recv(0);
                    if (reply.length > 0) {
                        final String str = new String(reply, ZMQ.CHARSET);
                        Platform.runLater(() ->
                                ChatPubSub.getInstance().publish(ChatConstants.EVENT_MESSAGE_RECV, str));
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
    }

    public Socket getSocket() {
        return socket;
    }

}

