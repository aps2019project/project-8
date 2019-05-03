package gen;

import javax.print.attribute.standard.NumberUp;
import java.io.File;
import java.util.Iterator;

class JsonMaker {

    private static void deleteAll(String dir) {
        File myDir = new File(dir);
        File[] files = myDir.listFiles();
        try {
            for (File file : files) {
                if (!file.delete()) {
                    System.out.println("Failed To delete!");
                }
            }
        } catch (NullPointerException ignore) {
            System.out.print("NULL POINTER EXCEPTION: ");
            System.out.println(dir);
        }
    }

    public static void main(String[] args) {
        deleteAll("./gameData/Usables/");
        deleteAll("./gameData/Heros/");
        deleteAll("./gameData/Minions/");
        deleteAll("./gameData/SpellCards/");
        deleteAll("./gameData/Collectibles");
        File dir = new File("./gameData/ManualFeatureInputLogs/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                ManualFeatureAdder.main(new String[]{"java", "ManualFeatureAdder", child.toString()});
            }
        }
    }
}
