package gen;

/*
 *  this class goes to ./ManualFeatureInputLogs and runs all of the files
 *  in it on Manual feature adder to make json files from it
 */

import java.io.File;
import java.util.Iterator;

class JsonMaker {
    public static void main(String[] args) {
        File dir = new File("./gameData/ManualFeatureInputLogs/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                ManualFeatureAdder.main(new String[] {"java", "ManualFeatureAdder", child.toString()});
            }
        }
    }
}
