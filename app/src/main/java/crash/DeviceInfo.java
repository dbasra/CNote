package crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by Michael on 2/14/2015.
 */
public class DeviceInfo {

    String VersionName;
    String PackageName;
    String FilePath;
    String PhoneModel;
    String AndroidVersion;
    String Board;
    String Brand;
    String Device;
    String Display;
    String FingerPrint;
    String Host;
    String ID;
    String Model;
    String Product;
    String Tags;
    long Time;
    String Type;
    String User;
    Context context;

    public DeviceInfo(Context context){
        this.context = context;
    }

    void collectInformation(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi;
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            VersionName = pi.versionName;
            PackageName = pi.packageName;
            FilePath = context.getFilesDir().getAbsolutePath();
            PhoneModel = android.os.Build.MODEL;
            AndroidVersion = android.os.Build.VERSION.RELEASE;
            Board = android.os.Build.BOARD;
            Brand = android.os.Build.BRAND;
            Device = android.os.Build.DEVICE;
            Display = android.os.Build.DISPLAY;
            FingerPrint = android.os.Build.FINGERPRINT;
            Host = android.os.Build.HOST;
            ID = android.os.Build.ID;
            Model = android.os.Build.MODEL;
            Product = android.os.Build.PRODUCT;
            Tags = android.os.Build.TAGS;
            Time = android.os.Build.TIME;
            Type = android.os.Build.TYPE;
            User = android.os.Build.USER;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public String CreateInformationString() {
        collectInformation(context);
        String separator = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append("Version : " + VersionName + separator);
        sb.append("Package : " + PackageName + separator + separator);
        sb.append("FilePath : " + FilePath + separator);
        sb.append("Phone Model : " + PhoneModel + separator);
        sb.append("Android Version : " + AndroidVersion + separator);
        sb.append("Board : " + Board + separator);
        sb.append("Brand : " + Brand + separator);
        sb.append("Device : " + Device + separator);
        sb.append("Display : " + Display + separator);
        sb.append("Finger Print : " + FingerPrint + separator);
        sb.append("Host : " + Host + separator);
        sb.append("ID : " + ID + separator);
        sb.append("Model : " + Model + separator);
        sb.append("Product : " + Product + separator);
        sb.append("Tags : " + Tags + separator);
        sb.append("Time : " + Time + separator);
        sb.append("Type : " + Type + separator);
        sb.append("User : " + User + separator);
        sb.append("Total Internal memory : " + getTotalInternalMemorySize() + separator);
        sb.append("Available Internal memory : " + getAvailableInternalMemorySize() + separator);
        return sb.toString();
    }

    public String getFilePath(){
        return context.getFilesDir().getAbsolutePath();
    }
}
