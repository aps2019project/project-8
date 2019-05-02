package gen;

/*
 *  this class goes to ./ManualFeatureInputLogs and runs all of the files
 *  in it on Manual feature adder to make json files from it
 */

import java.io.File;
import java.util.Iterator;

class JsonMaker {

    private static void deleteAll(String dir) {
        File myDir = new File(dir);
        File[] files = myDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.delete()) {
                    System.out.println("Failed To delete!");
                }
            }
        }
    }
    public static void main(String[] args) {
        deleteAll("./gamaData/Collectibles/");
        deleteAll("./gameData/Heros/");
        deleteAll("./gameData/Minions/");
        deleteAll("./gameData/SpellCards/");
        File dir = new File("./gameData/ManualFeatureInputLogs/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                ManualFeatureAdder.main(new String[] {"java", "ManualFeatureAdder", child.toString()});
            }
        }
    }
}
