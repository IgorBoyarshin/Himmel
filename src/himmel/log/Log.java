package himmel.log;

/**
 * Created by Igor on 24-Apr-15.
 */
public class Log {
    public static void logError(String error) {
        System.err.println(":> Error: " + error);
    }

    public static void logInfo(String info) {
        System.out.println(":> Info: " + info);
    }
}
