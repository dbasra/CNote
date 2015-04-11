package crash;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Michael on 2/14/2015.
 */
public class UtilFunctions {

    public static String readLog(Context context) {
        String command = "logcat -d -v time";
        Process mLogcatProc = null;
        BufferedReader reader = null;
        StringBuilder log = null;
        try {
            mLogcatProc = Runtime.getRuntime().exec(command);
            reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));
            String line;
            log = new StringBuilder();
            String separator = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                log.append(line);
                log.append(separator);
            }
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {}
            }
        }
        return log.toString();
    }
}
