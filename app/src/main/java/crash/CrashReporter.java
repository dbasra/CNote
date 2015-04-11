package crash;

import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

/**
 * Created by Michael on 2/14/2015.
 */
public class CrashReporter implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler perviousHandler;
    private static CrashReporter instance;
    private Context mContext;
    String filePath;
    DeviceInfo deviceInfo;
    String separator;
    private Object Constant = "mkalu@terpmail.umd.edu";

    public static CrashReporter getInstance() {
        if (instance == null)
            instance = new CrashReporter();
        return instance;
    }

    public void init(Context context) {
        perviousHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        deviceInfo = new DeviceInfo(context);
        filePath = deviceInfo.getFilePath();
        separator = System.getProperty("line.separator");
        mContext = context;
    }

    public void uncaughtException(Thread t, Throwable e) {
        StringBuilder infoString = new StringBuilder();

        String time = new Date().toString();
        String deviceInformation = deviceInfo.CreateInformationString();
        String stackTrace = readStackTrace(e);
        String stackCause = getStackCause(e);
        String logcat = UtilFunctions.readLog(mContext);

        infoString.append("****  Start of current Report ***");
        infoString.append("Error Report collected on : " + time + separator);
        infoString.append("Informations :" + separator + "=>" + separator + deviceInformation + separator);
        infoString.append("StackTrace : " + separator + "=>" + separator + stackTrace + separator);
        infoString.append("StackCause : " + separator + "=>" + separator + stackCause + separator);
        infoString.append("Logcat : " + separator + "=>" + separator + logcat + separator);
        infoString.append("****  End of current Report ***");
        saveAsFile(infoString.toString());
        perviousHandler.uncaughtException(t, e);
    }

    private String readStackTrace(Throwable e) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        return result.toString();
    }

    private String getStackCause(Throwable e) {
        StringBuffer sb = new StringBuffer();
        Throwable cause = e.getCause();
        while (cause != null) {
            sb.append(readStackTrace(cause));
            sb.append(separator);
            cause = cause.getCause();
        }
        return sb.toString();
    }

    private void sendErrorMail(String ErrorContent) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        String subject = "Error Report";
        String body = ErrorContent;
        sendIntent.putExtra(Intent.EXTRA_EMAIL,   new String[] {Constant.toString()});
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.setType("message/rfc822");
        mContext.startActivity(Intent.createChooser(sendIntent, "Title:"));
    }

    private void saveAsFile(String ErrorContent) {
        try {
            long random = System.currentTimeMillis();
            String FileName = "stack-" + random + ".stacktrace";
            FileOutputStream trace = mContext.openFileOutput(FileName, Context.MODE_PRIVATE);
            trace.write(ErrorContent.getBytes());
            trace.close();
        } catch (IOException e) {}
    }

    public String[] getErrorFileList() {
        File dir = new File(filePath + "/");
        dir.mkdir();
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".stacktrace");
            }
        };
        return dir.list(filter);
    }

    public boolean isThereAnyErrorFile() {
        return getErrorFileList().length > 0;
    }

    public void checkErrorAndSendMail() {
        try {
            if (isThereAnyErrorFile()) {
                String wholeErrorText = "";
                String[] ErrorFileList = getErrorFileList();
                int curIndex = 0;
                final int MaxSendMail = 5;
                for (String curString : ErrorFileList) {
                    if (curIndex++ <= MaxSendMail) {
                        wholeErrorText += "Trace collected :" + separator;
                        wholeErrorText += "=====================" + separator;
                        String fPath = filePath + "/" + curString;
                        BufferedReader input = new BufferedReader( new FileReader(fPath));
                        String line;
                        while ((line = input.readLine()) != null) {
                            wholeErrorText += line + "\n";
                        }
                        input.close();
                    }

                    File curFile = new File(filePath + "/" + curString);
                    curFile.delete();
                }
                sendErrorMail(wholeErrorText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
