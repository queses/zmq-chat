import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;
import org.zeromq.ZFrame;;

/**
 * Сервер можно сделать мультипоточным:
 * https://github.com/zeromq/jeromq/blob/master/src/test/java/guide/mtserver.java
 */
public class Main {
    public static void main(String[] args) throws Exception
    {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to clients
        ZMQ.Socket socket = context.socket(ZMQ.PULL);
        ZMQ.Socket displaySocket = context.socket(ZMQ.PUB);

        socket.bind("tcp://*:5555");
        displaySocket.bind("tcp://*:5556");

        while (!Thread.currentThread().isInterrupted()) {

            byte[] reply = socket.recv(0);
            String str = new String(reply, ZMQ.CHARSET);
            System.out.println("Received " + ": [ " + str + " ]");
            
            displaySocket.send("[main]" + str, 0);

            Thread.sleep(100); //  Do some 'work'
        }

        socket.close();
        context.term();
    }

    public static void _main(String[] args)
    {
        if (args.length < 1) {
            System.out.printf("I: syntax: flserver1 <endpoint>\n");
            System.exit(0);
        }
        ZContext ctx = new ZContext();
        Socket server = ctx.createSocket(ZMQ.REP);
        server.bind(args[0]);

        System.out.printf("I: echo service is ready at %s\n", args[0]);
        while (true) {
            ZMsg msg = ZMsg.recvMsg(server);
            if (msg == null)
                break; //  Interrupted
            msg.send(server);
        }
        if (Thread.currentThread().isInterrupted())
            System.out.printf("W: interrupted\n");

        ctx.close();
    }

    public static void __main(String[] args)
    {
        if (args.length < 1) {
            System.out.printf("I: syntax: flserver2 <endpoint>\n");
            System.exit(0);
        }
        ZContext ctx = new ZContext();
        Socket server = ctx.createSocket(ZMQ.REP);
        server.bind(args[0]);

        System.out.printf("I: echo service is ready at %s\n", args[0]);
        while (true) {
            ZMsg request = ZMsg.recvMsg(server);

            System.out.print("BOis");
            if (request == null)
                break; //  Interrupted

            //  Fail nastily if run against wrong client
            assert (request.size() == 2);

            ZFrame identity = request.pop();
            request.destroy();

            ZMsg reply = new ZMsg();
            reply.add(identity);
            reply.add("OK");
            reply.send(server);
        }
        if (Thread.currentThread().isInterrupted())
            System.out.printf("W: interrupted\n");

        ctx.close();
    }
}
