package client;

import com.gilecode.yagson.com.google.gson.JsonArray;
import com.gilecode.yagson.com.google.gson.JsonObject;
import com.gilecode.yagson.com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class AnonymousConnection {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public AnonymousConnection(Socket socket, PrintWriter out, BufferedReader in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
    }

    private JsonObject getAsJson(String message) {
        JsonParser parser = new JsonParser();
        return (JsonObject) parser.parse(message);
    }


    public String[] getLeaderBoard() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestType", "getLeaderBoard");
        out.println(jsonObject.toString());
        try {
            String response = in.readLine();
            jsonObject = getAsJson(response);
            JsonArray jsonArray = (JsonArray) jsonObject.get("leaderBoard");
            System.err.println(jsonArray.toString());
            String[] names = new String[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++)
                names[i] = jsonArray.get(i).getAsString();
            return names;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("error occurred in fetching response from server");
            return new String[]{"error occurred"};
        }
    }
}
