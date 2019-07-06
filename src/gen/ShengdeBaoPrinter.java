package gen;

public class ShengdeBaoPrinter {
    private static String[] defaultPrefix = new String[20];
    private static int numberOfPrefixes = 0;

    public static void undo() {
        numberOfPrefixes--;
        defaultPrefix[numberOfPrefixes] = null;
    }

    public static void addString(String string) {
        defaultPrefix[numberOfPrefixes] = string;
        numberOfPrefixes++;
    }

    public static void addTab() {
        addString("\t");
    }

    public static void print(String... args) {
        for (String prefix: defaultPrefix)
            if (prefix != null)
                System.out.print(prefix);
        int i = 0;
        for (String arg : args) {
            if (i > 0)
                System.out.print(" ");
            System.out.print(arg);
            i++;
        }
    }

    public static void println(String... args) {
        print(args);
        System.out.print("\n");
    }
}

