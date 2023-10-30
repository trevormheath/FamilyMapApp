package Handler;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class FileHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")){
                String urlPath = exchange.getRequestURI().toString();

                if(urlPath.equals("/") || urlPath.isEmpty()){
                    urlPath = "/index.html";
                }

                String filePath = "web" + urlPath;
                File file = new File(filePath);

                //Access the file if it's somewhere in the directory
                if(file.exists()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();

                    Files.copy(file.toPath(), respBody);
                    respBody.close();
                    success = true;
                }
            }

            if(!success){
                //if it wasn't a success and the web page wasn't found then return the 404 page instead
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                OutputStream respBody = exchange.getResponseBody();

                String filePath = "web/HTML/404.html";
                File file = new File(filePath);
                Files.copy(file.toPath(), respBody);
                respBody.close();
                exchange.getResponseBody().close();
            }
        } catch (IOException ex){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            exchange.getResponseBody().close();
            ex.printStackTrace();
        }
    }
}
