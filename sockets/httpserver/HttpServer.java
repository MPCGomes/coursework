package httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServer {

    public static void main(String[] args) throws IOException {
        int port = 8000;
        ServerSocket serverSocket = new ServerSocket(port);

        while (!serverSocket.isClosed()) {
            Socket clientSocket = serverSocket.accept();

            String html = "<html><head><title>Servidor Java</title></head>"
                    + "<body><h1>Olá, mundo bão!</h1></body></html>";

            byte[] bodyBytes = html.getBytes(StandardCharsets.UTF_8);

            String response = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html; charset=UTF-8\r\n"
                    + "Content-Length: " + bodyBytes.length + "\r\n"
                    + "Connection: close\r\n\r\n";

            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
            outputStream.write(bodyBytes);
            outputStream.flush();

            clientSocket.close();
        }

        serverSocket.close();
    }
}
