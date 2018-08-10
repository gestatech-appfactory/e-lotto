package be.gestatech.elotto.business.configuration.control;

import be.gestatech.elotto.infrastructure.persistence.DBConnection;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
@Startup
@DependsOn("DBConnection")
@Named
public class ConfigurationController implements Serializable {

    private static final long serialVersionUID = 6962217699259540044L;

    private static final Logger logger = Logger.getLogger(ConfigurationController.class.getName());

    @Inject
    DBConnection dbConnection;

    private Map<String, String> configCache = new HashMap<>();

    public ConfigurationController() {
        // Intentionally left blank
    }

    @PostConstruct
    public void initializeConfiguration() {

        MongoDatabaseConfiguration config = new MongoDatabaseConfiguration(dbConnection.getMongoDatabase());

        // initialize cache
        String appserverConfigName = System.getenv("lottoritter_jvmRoute");
        Properties baseConfig = config.getKeysForConfigurationKey(MongoDatabaseConfiguration.CKEY_VALUE_BASE_CONFIGURATION);
        if (baseConfig.isEmpty()) {
            baseConfig = config.prepareInitialBaseConfiguration();
        }
        for (Map.Entry<Object, Object> objectObjectEntry : baseConfig.entrySet()) {
            configCache.put(objectObjectEntry.getKey().toString(), objectObjectEntry.getValue().toString());
        }

        Properties appServerConfig = config.getKeysForConfigurationKey(appserverConfigName);
        for (Map.Entry<Object, Object> objectObjectEntry : appServerConfig.entrySet()) {
            configCache.put(objectObjectEntry.getKey().toString(), objectObjectEntry.getValue().toString());
        }
        StringBuilder builder = new StringBuilder("Starting with configuration:\n");
        for (Map.Entry<String, String> entry : getConfigCache().entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
        }
        if (logger.isLoggable(Level.INFO)) {
            logger.info(builder.toString());
        }
    }

    public void refreshConfiguration() {
        getConfigCache().clear();
        initializeConfiguration();
    }


    @Produces
    @Configurable(value = "", defaultValue = "")
    public String getString(InjectionPoint ip) {
        String className = ip.getMember().getDeclaringClass().getName();
        String key = className + "." + ip.getMember().getName();
        String fieldName = computeKeyName(ip.getAnnotated(), key);
        String value = this.configCache.get(fieldName);
        if (Objects.isNull(value)) {
            value = ip.getAnnotated().getAnnotation(Configurable.class).defaultValue();
        }
        return value;
    }

    @Produces
    @Configurable(value = "", defaultValue = "0")
    public long getLong(InjectionPoint ip) {
        String stringValue = getString(ip);
        if (Objects.isNull(stringValue)) {
            return 0;
        }
        return Long.parseLong(stringValue);
    }

    @Produces
    @Configurable(value = "", defaultValue = "false")
    public Boolean getBoolean(InjectionPoint ip) {
        String stringValue = getString(ip);
        if (Objects.isNull(stringValue)) {
            return false;
        }
        return Boolean.valueOf(stringValue);
    }

    @Produces
    @Configurable(value = "mail_settings", defaultValue = "")
    public Properties getMailSmtpSettings() {
        Properties props = new Properties();
        getConfigCache().entrySet().stream().filter(p -> p.getKey().startsWith("mail_smtp")).collect(Collectors.toSet()).forEach(p -> props.setProperty(p.getKey().replaceAll("_", "."), String.valueOf(p.getValue())));
        return props;
    }

    @Produces
    @Configurable(value = "primaryPSP", defaultValue = "NO_OP")
    public PspCode getPrimaryPaymentServiceProvider(InjectionPoint ip) {
        try {
            return PspCode.valueOf(getString(ip));
        } catch (IllegalArgumentException ex) {
            logger.severe("PSP-code not recognized. Using NO_OP as fallback.");
        }
        return PspCode.NO_OP;
    }

    private String computeKeyName(Annotated annotated, String key) {
        Configurable annotation = annotated.getAnnotation(Configurable.class);
        return Objects.isNull(annotation) ? key : annotation.value();
    }

    private Map<String, String> getConfigCache() {
        return configCache;
    }

}
