package org.graylog.alarmcallbacks.clickatell;

import org.graylog2.plugin.alarms.AlertCondition;
import org.graylog2.plugin.alarms.callbacks.AlarmCallback;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackConfigurationException;
import org.graylog2.plugin.alarms.callbacks.AlarmCallbackException;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.configuration.ConfigurationException;
import org.graylog2.plugin.configuration.ConfigurationRequest;
import org.graylog2.plugin.streams.Stream;
import com.google.common.collect.ImmutableList;
import org.graylog2.plugin.configuration.fields.ConfigurationField;
import org.graylog2.plugin.configuration.fields.TextField;
import com.google.common.collect.Maps;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient;

import java.util.Map;
import java.io.IOException;
import java.util.List;

import static java.lang.Math.min;

public class ClickatellSMSAlarmCallback implements AlarmCallback {

    private static final Logger LOG = LoggerFactory.getLogger(ClickatellSMSAlarmCallback.class);

    private static final int MAX_MSG_LENGTH = 140;

    private static final String API_ENDPOINT = "https://platform.clickatell.com/messages/http/send";

    private static final String CK_API_KEY = "api_key";
    private static final String CK_TO_NUMBER = "to_number";

    private static final String[] MANDATORY_CONFIGURATION_KEYS = new String[] {
            CK_API_KEY, CK_TO_NUMBER
    };
    private static final List<String> SENSITIVE_CONFIGURATION_KEYS = ImmutableList.of(CK_API_KEY);

    private Configuration config;
    private final OkHttpClient httpClient = new OkHttpClient();

    @Override
    public void initialize(Configuration config) throws AlarmCallbackConfigurationException {
        this.config = config;
    }

    @Override
    public void call(Stream stream, AlertCondition.CheckResult result) throws AlarmCallbackException {
        try {
            send(stream, result);
        } catch (Exception e) {
            LOG.error("Could not send alarm via Twilio SMS", e);
        }
    }

    @Override
    public ConfigurationRequest getRequestedConfiguration() {
        final ConfigurationRequest cr = new ConfigurationRequest();

        cr.addField(new TextField(CK_API_KEY, "API Key", "", "Clickatell API key",
                ConfigurationField.Optional.NOT_OPTIONAL));

        cr.addField(new TextField(CK_TO_NUMBER, "Recipient Phone Number", "",
                "The phone number of the recipient of the SMS.",
                ConfigurationField.Optional.NOT_OPTIONAL));
        return cr;
    }

    @Override
    public String getName() {
        return "Clickatell SMS Alarmcallback";
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Maps.transformEntries(config.getSource(), new Maps.EntryTransformer<String, Object, Object>() {
            @Override
            public Object transformEntry(String key, Object value) {
                if (SENSITIVE_CONFIGURATION_KEYS.contains(key)) {
                    return "****";
                }
                return value;
            }
        });
    }

    @Override
    public void checkConfiguration() throws ConfigurationException {
        for (String key : MANDATORY_CONFIGURATION_KEYS) {
            if (!config.stringIsSet(key)) {
                throw new ConfigurationException(key + " is mandatory and must not be empty.");
            }
        }
    }

    private void send(Stream stream, AlertCondition.CheckResult result) throws Exception {

        String url = API_ENDPOINT + "?apiKey=" + config.getString(CK_API_KEY) + "&to=" +
                config.getString(CK_TO_NUMBER)
                + "&content=" + buildMessage(result);

        final Request request = new Request.Builder()
                .url(url)
                .build();

        try (final Response r = httpClient.newCall(request).execute()) {
            if (!r.isSuccessful()) {
                // ideally this should not happen and the user is expected to fill the
                // right configuration , while setting up a notification
                throw new Exception(
                        "Expected successful HTTP response [2xx] but got [" + r.code() + "]. " +
                                url);
            }
            LOG.debug("Sent SMS with status {}: {}", r.code(), r.message());
        } catch (IOException e) {
            throw new Exception("Unable to send the Message. " + e.getMessage());
        }
    }

    private String buildMessage(final AlertCondition.CheckResult result) {
        final String msg = "[Graylog] " + result.getResultDescription();

        return msg.substring(0, min(msg.length(), MAX_MSG_LENGTH));
    }
}
