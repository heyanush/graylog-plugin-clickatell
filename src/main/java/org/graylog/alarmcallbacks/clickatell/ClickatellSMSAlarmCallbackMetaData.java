package org.graylog.alarmcallbacks.clickatell;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

/**
 * Implement the PluginMetaData interface here.
 */
public class ClickatellSMSAlarmCallbackMetaData implements PluginMetaData {
    private static final String PLUGIN_PROPERTIES = "org.graylog.alarmcallbacks.clickatell/clickatell.properties";

    @Override
    public String getUniqueId() {
        return ClickatellSMSAlarmCallback.class.getCanonicalName();
    }

    @Override
    public String getName() {
        return "Clickatell SMS Alarmcallback Plugin";
    }

    @Override
    public String getAuthor() {
        return "Graylog, Inc <hello@graylog.com>";
    }

    @Override
    public URI getURL() {
        return URI.create("https://github.com/Graylog2/graylog-plugin-sample");
    }

    @Override
    public Version getVersion() {
        return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "version", Version.from(0, 0, 0, "unknown"));
    }

    @Override
    public String getDescription() {
        return "Alarm callback plugin that sends all stream alerts as SMS to a defined phone number.";
    }

    @Override
    public Version getRequiredVersion() {
        return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "graylog.version", Version.from(0, 0, 0, "unknown"));
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
