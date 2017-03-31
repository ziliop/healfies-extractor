package com.healfies.services.configuration;

import java.io.IOException;
import java.util.Properties;

public class HealfiesExtractorProperties {

    private static HealfiesExtractorProperties hProperties = null;

    private Properties prop = new Properties();

    private HealfiesExtractorProperties() {
        try {
            prop.load(this.getClass().getResourceAsStream("/healfies-extractor.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HealfiesExtractorProperties getInstance() {
        if (hProperties == null) {
            hProperties = new HealfiesExtractorProperties();
        }
        return hProperties;

    }

    public String getSchedulerTime() {
        return prop.getProperty("com.healfies.extractor.scheduler");
    }

    public String getTopicName() {
        return prop.getProperty("com.healfies.extractor.client.id");
    }

    public String getProjectID() {
        return prop.getProperty("com.healfies.extractor.google.project.id");
    }

}
