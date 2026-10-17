package io.stealingdapenta.mc2048.config;

import java.util.Map;

public final class ConfigurationValidationException extends IllegalArgumentException {

    private final Map<ConfigKey, String> errors;

    ConfigurationValidationException(Map<ConfigKey, String> errors) {
        super("Invalid MC2048 configuration: " + String.join("; ", errors.values()));
        this.errors = Map.copyOf(errors);
    }

    public Map<ConfigKey, String> getErrors() {
        return errors;
    }
}
