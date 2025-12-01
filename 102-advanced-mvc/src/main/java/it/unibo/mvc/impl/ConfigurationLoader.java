package it.unibo.mvc.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import it.unibo.mvc.impl.Configuration.Builder;

/**
 * A simple configuration loader that loads data from file.
 */
public final class ConfigurationLoader {
    
    private static final String CONFIG_FILE = "config.yml";

    private ConfigurationLoader() {}

    public static Configuration load() {

        final Builder configBuilder = new Builder();

        BufferedReader br = new BufferedReader(
            new InputStreamReader(
                ConfigurationLoader.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)
            )
        );

        String line;
        try {
            while ((line = br.readLine()) != null) {
                final StringTokenizer st = new StringTokenizer(line, ":");
                if (st.countTokens() == 2) {
                    final String key = st.nextToken().trim();
                    final int value = Integer.parseInt(st.nextToken().trim());
                    switch (key) {
                        case "minimum" -> configBuilder.withMin(value);
                        case "maximum" -> configBuilder.withMax(value);
                        case "attempts" -> configBuilder.withAttempts(value);
                    }
                }
            }
        } catch (final IOException e) {
            throw new IllegalStateException("Error reading configuration file", e);
        }

        return configBuilder.build();
    }

}
