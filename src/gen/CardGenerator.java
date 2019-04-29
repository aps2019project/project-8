package gen;

import com.google.gson.Gson;
import model.Card;

import java.io.FileWriter;
import java.io.IOException;

public class CardGenerator {
    public static void main(String[] args) {
        int n = 100;
        for (int i = 0; i < n; i++) {
            saveCard(new Card(i, i, String.valueOf(i), i));
        }
    }
    private static void saveCard(Card card) {
        try {
            FileWriter out = new FileWriter("./gameData/cards/" + card.getName() + ".txt", false);
            Gson gson = new Gson();
            out.write(gson.toJson(card));
            out.flush();
        } catch (IOException ignored) {}
    }
}
