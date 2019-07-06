package gen;

import java.io.File;

public class JsonMaker {

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
        File logFile = new File("./gameData/ManualFeatureInputLogs/tempLog.txt");
        try {
            logFile.delete();
        }catch (Exception exception) {
            System.out.println("tempLog.txt didn't exist to delete");
        }

        deleteAll("./gameData/Usables/");
        deleteAll("./gameData/Heroes/");
        deleteAll("./gameData/Minions/");
        deleteAll("./gameData/SpellCards/");
        deleteAll("./gameData/Collectibles/");
        File dir = new File("./gameData/ManualFeatureInputLogs/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                System.err.println("child name "  + child.toString());
                ManualFeatureAdder.main(new String[]{"java", "ManualFeatureAdder", child.toString()});
            }
        }
    }
}
