package Handler;

import Request.RegisterRequest;
import Result.RegisterResult;
import Service.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.HttpURLConnection;

public class RegisterHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        Gson gson = new Gson();
        try {
            if(exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                RegisterRequest req = gson.fromJson(reqData, RegisterRequest.class);
                RegisterService serv = new RegisterService();
                //Register them and generate a AuthToken for the new user if no error was thrown
                RegisterResult result = serv.doRegister(req);
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
/*
            exchange
                    User newReg = new User();
                    new UserDao(db.getConnection()).insert(newReg);
                    //Fill the username with 4 generations
                    FillService fServ = new FillService();
                    fServ.doFill(newReg.getUsername(), 4, db.getConnection());

                    //Login the New User after tree has been filled
                    LoginService lServ = new LoginService();
                    lServ.doLogin();
*/



/*
// Read JSON string from the input stream
String reqData = readString(reqBody);

LoginRequest request = (LoginRequest)gson.fromJson(reqData, LoginRequest.class);*/
