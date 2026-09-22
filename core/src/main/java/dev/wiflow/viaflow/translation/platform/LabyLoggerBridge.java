package dev.wiflow.viaflow.translation.platform;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import net.labymod.api.util.logging.Logging;

/**
 * Routes ViaVersion's java.util.logging output into the LabyMod log.
 */
final class LabyLoggerBridge extends Logger {

    private final Logging logging;

    LabyLoggerBridge(String name) {
        super(name, null);
        this.logging = Logging.create(name);
    }

    @Override
    public void log(LogRecord record) {
        if (!this.isLoggable(record.getLevel())) {
            return;
        }

        String message = format(record);
        Throwable thrown = record.getThrown();
        int level = record.getLevel().intValue();
        if (level >= Level.SEVERE.intValue()) {
            if (thrown != null) {
                this.logging.error(message, thrown);
            } else {
                this.logging.error(message);
            }
        } else if (level >= Level.WARNING.intValue()) {
            if (thrown != null) {
                this.logging.warn(message, thrown);
            } else {
                this.logging.warn(message);
            }
        } else if (level >= Level.INFO.intValue()) {
            if (thrown != null) {
                this.logging.info(message, thrown);
            } else {
                this.logging.info(message);
            }
        } else if (thrown != null) {
            this.logging.debug(message, thrown);
        } else {
            this.logging.debug(message);
        }
    }

    private static String format(LogRecord record) {
        String message = record.getMessage();
        if (message == null) {
            return "";
        }

        Object[] parameters = record.getParameters();
        if (parameters == null || parameters.length == 0) {
            return message;
        }

        try {
            return MessageFormat.format(message, parameters);
        } catch (IllegalArgumentException e) {
            return message;
        }
    }
}
