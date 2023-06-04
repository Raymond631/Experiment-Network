import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * @author Raymond Li
 * @create 2023-04-23 19:06
 * @description
 */
public class Server {
    public static void main(String[] args) throws IOException {
        try (ServerSocket ss = new ServerSocket(6666)) {
            System.out.println("Server is running...");
            while (true) {
                Socket sock = ss.accept();
                System.out.println("Connected from " + sock.getRemoteSocketAddress());
                Thread t = new Handler(sock);
                t.start();
            }
        }
    }
}

class Handler extends Thread {
    private Socket sock;

    public Handler(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        try (InputStream input = this.sock.getInputStream()) {
            try (OutputStream output = this.sock.getOutputStream()) {
                handle(input, output);
            }
        } catch (Exception e) {
            try {
                this.sock.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Client from" + sock.getRemoteSocketAddress() + " disconnected.");
        }
    }

    private void handle(InputStream input, OutputStream output) throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        writer.write("hello\n");
        writer.flush();
        while (true) {
            String s = reader.readLine();
            System.out.println("[" + sock.getRemoteSocketAddress() + "] " + s);
            if ("bye".equals(s)) {
                writer.write("bye\n");
                writer.flush();
                break;
            }
            writer.write("Server received : " + s + "\n");
            writer.flush();
        }
    }
}
