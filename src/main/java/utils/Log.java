package utils;


public class Log {
    public static final boolean DEBUG = false;

    public static void d(String output) {
        if (DEBUG) {
            System.out.println(output);
        }
    }

    public static void e(Exception e) {
        e.printStackTrace();
    }
}
