package de.myo.myoscriptcontrol.scriptexecution;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import de.myo.myoscriptcontrol.activities.MainActivity;
import de.myo.myoscriptcontrol.FileManager;
import de.myo.myoscriptcontrol.listmanagement.scriptmanagement.ScriptItem;

/**
 * Created by DTH on 30.03.2015.
 */
public class SL4AManager {
    public static void startScript(Context appContext, Context activityContext, ScriptItem scriptItem)throws IOException{
        if (neededPackagesExist(appContext)){
            File file = new File(MainActivity.ScriptDir ,scriptItem.getScriptFile());
            File outputDir = appContext.getExternalCacheDir();
                File outputFile = File.createTempFile(scriptItem.getId().toString(), "."+scriptItem.getScriptFileType(), outputDir);
                FileManager.copyFile(file, outputFile);
                if (!outputFile.exists()){
                    throw new FileNotFoundException("Das auszuführende Skript konnte nicht gefunden werden. \n\n Importieren Sie es ggfs. erneut.");
                }
                Intent intent = buildStartSL4A(outputFile);
                activityContext.startActivity(intent);
        } else {
            throw new IOException("Die notwendigen Anwendungen \"SL4A\" bzw. \"Python for Android\" zum Ausrühren von Scripten konnten nicht gefunden werden.");
        }
    }

    private static Intent buildStartSL4A(File file){
        final ComponentName componentName = SL4AConstants.SL4A_SERVICE_LAUNCHER_COMPONENT_NAME;
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.setAction(SL4AConstants.ACTION_LAUNCH_BACKGROUND_SCRIPT);
        intent.putExtra(SL4AConstants.EXTRA_SCRIPT_PATH, file.getAbsolutePath());
        return intent;
    }

    private static boolean neededPackagesExist(Context context){
        final PackageManager pm = context.getPackageManager();
        boolean androidScriptingExists = false;
        boolean pythonForAndroidExists = false;
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages){
            if (packageInfo.packageName.equalsIgnoreCase("com.googlecode.android_scripting")){
                androidScriptingExists = true;
            }
            if (packageInfo.packageName.equalsIgnoreCase("com.googlecode.pythonforandroid")){
                pythonForAndroidExists = true;
            }
        }
        return androidScriptingExists & pythonForAndroidExists;
    }

}
