package himmel.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Igor on 01-May-15.
 */
public class FileLoader {
    public static String loadAsString(String file) {
        String result = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String buffer = "";
            while((buffer = reader.readLine()) != null) {
                result += buffer + "\n";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
