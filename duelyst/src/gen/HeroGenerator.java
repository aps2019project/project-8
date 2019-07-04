package gen;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import model.Hero;

import java.io.FileWriter;
import java.io.IOException;

public class HeroGenerator {
    public static void main(String[] args) {
        int n = 100;
        for (int i = 0; i < n; i++) {
         //   saveHero(new Hero(i, String.valueOf(i), String.valueOf(i), i));
        }
    }
    private static void saveHero(Hero hero) {
        try {
            FileWriter out = new FileWriter("./gameData/heroes/" + hero.getName() + ".txt", false);
            YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
            out.write(yaGson.toJson(hero, Hero.class));
            out.flush();
        } catch (IOException ignored) {
            System.out.println("Can't Read file for some reason: ");
            System.out.println("File can't be created / File can't be opened / A directory rather than file");
        }
    }
}
