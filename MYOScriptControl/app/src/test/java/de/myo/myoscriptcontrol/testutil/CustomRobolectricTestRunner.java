package de.myo.myoscriptcontrol.testutil;


import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.SdkConfig;
import org.robolectric.SdkEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.Properties;

public class CustomRobolectricTestRunner extends RobolectricTestRunner {
    public CustomRobolectricTestRunner(Class<?> testClass)

            throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        String path = "src/main/AndroidManifest.xml";
        if (!new File(path).exists()) {
            path = "app/" + path;
        }
        config = overwriteConfig(config, "manifest", path);
        return super.getAppManifest(config);
    }

    protected Config.Implementation overwriteConfig(Config config, String key, String value) {
        Properties properties = new Properties();
        properties.setProperty(key, value);
        return new Config.Implementation(config, Config.Implementation.fromProperties(properties));
    }



    @Override
    protected SdkConfig pickSdkVersion(AndroidManifest appManifest, Config config) {
        config = overwriteConfig(config, "emulateSdk", "18");
        return super.pickSdkVersion(appManifest, config);
    }

    @Override
    protected void configureShadows(SdkEnvironment sdkEnvironment, Config config) {
        Properties properties = new Properties();
        properties.setProperty("shadows", ShadowSupportMenuInflater.class.getName());
        super.configureShadows(sdkEnvironment, new Config.Implementation(config, Config.Implementation.fromProperties(properties)));
    }


}

