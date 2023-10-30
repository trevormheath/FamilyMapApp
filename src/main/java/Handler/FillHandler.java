package Handler;

import Result.FillResult;
import Service.FillService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class FillHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        Gson gson = new Gson();
        try {
            if(exchange.getRequestMethod().toLowerCase().equals("post")){

                //Retrieve both the username and possibly number of generations from the URI
                String url = exchange.getRequestURI().toString();
                String[] arguments = url.split("/");

                String username = arguments[2];
                int numGen;
                if(arguments.length > 3){
                    numGen = Integer.parseInt(arguments[3]);
                }
                else {
                    //if generations wasn't present then use 4 as the default
                    numGen = 4;
                }

                FillService serv = new FillService();
                FillResult result;
                result = serv.doFill(username, numGen);
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
