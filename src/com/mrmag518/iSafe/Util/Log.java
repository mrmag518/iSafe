package com.mrmag518.iSafe.Util;

import com.mrmag518.iSafe.Files.iSafeConfig;
import java.util.logging.Logger;

public class Log {
    private static final Logger log = Logger.getLogger("Minecraft");
    
    private static final String PREFIX = "[iSafe] ";
    private static final String DEBUG_PREFIX = "[iSafe] [DEBUG] ";
    
    public static void info(String output) {
        if(output.contains("[iSafe]")) { // I'm lazy .. Fixing all loggers manually later.
            log.info(output);
            return;
        }
        log.info(PREFIX + output);
    }
    
    public static void severe(String output) {
        if(output.contains("[iSafe]")) {
            log.severe(output);
            return;
        }
        log.severe(PREFIX + output);
    }
    
    public static void warning(String output) {
        if(output.contains("[iSafe]")) {
            log.warning(output);
            return;
        }
        log.warning(PREFIX + output);
    }
    
    public static void verbose(String output) {
        if (verboseLogging() == true) {
            log.info(PREFIX + output);
        }
    }

    public static void debug(String output) {
        if (debugMode() == true) {
            log.info(DEBUG_PREFIX + output);
        }
    }
    
    private static boolean verboseLogging() {
        return iSafeConfig.getISafeConfig().getBoolean("VerboseLogging");
    }

    private static boolean debugMode() {
        return iSafeConfig.getISafeConfig().getBoolean("DebugMode");
    }
}
