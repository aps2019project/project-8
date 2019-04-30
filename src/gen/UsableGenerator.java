package gen;

import com.google.gson.Gson;
import model.Usable;

import java.io.FileWriter;
import java.io.IOException;

public class UsableGenerator {
    public static void main(String[] args) {
        int n = 100;
        for (int i = 0; i < n; i++) {
            saveUsable(new Usable(i + 1000, i + 1000, String.valueOf(i + 1000), null));
        }
    }
    private static void saveUsable(Usable usable) {
        try {
            FileWriter out = new FileWriter("./gameData/usables/" + usable.getName() + ".txt", false);
            Gson gson = new Gson();
            out.write(gson.toJson(usable));
            out.flush();
        } catch (IOException ignored) {
            System.out.println("Can't Read file for some reason: ");
            System.out.println("File can't be created / File can't be opened / A directory rather than file");
        }
    }
}
