package Handler;

import Request.LoginRequest;
import Result.LoginResult;
import Service.LoginService;
import com.google.gson.*;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.HttpURLConnection;

public class LoginHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        Gson gson = new Gson();
        try {
            if(exchange.getRequestMethod().toLowerCase().equals("post")) {

                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                LoginRequest req = gson.fromJson(reqData, LoginRequest.class);

                LoginService serv = new LoginService();
                LoginResult result = serv.doLogin(req);
                //Based on the success variable in result return appropriate headers
                if (result.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                //Output the data to the website server
                OutputStream resBody = exchange.getResponseBody();
                String resData = gson.toJson(result);
                writeString(resData, resBody);

                resBody.close();
                exchange.getResponseBody().close();
                success = true;
            }
            if(!success){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        } catch(IOException ex){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            exchange.getResponseBody().close();
            ex.printStackTrace();
        }
    }
}
