package com.healfies.services.client;

import java.io.IOException;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.google.pubsub.v1.TopicName;
import com.healfies.services.configuration.HealfiesExtractorProperties;

public class Main {

    private static final int ERR_START = 7;

    private static HealfiesExtractorProperties prop;

    public static void main(String[] args) throws IOException {

        // check configuration file and make sure it's correct
        loadConfigurationFile();

        Publish p = new Publish();

        TopicName topic = p.createOrRetrieveTopic();
        if (topic != null) {
            // publish message
            p.publish(topic, "mensagem inicial");

            Subscribe s = new Subscribe();
            s.pull(topic, "client-1");

            // everything is ready, start the server
            // verificarEIniciar();
        }

    }

    private static void verificarEIniciar() {

        // logger.trace("Starting the Openstack event collector micro service");
        try {

            // also start collection immediately
            Integer time = Integer.valueOf(prop.getSchedulerTime());

            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            JobDetail job = JobBuilder.newJob(ListenerJob.class).withIdentity("job1", "group1").build();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startNow()
                .withSchedule(
                    SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(time).repeatForever())
                .build();
            scheduler.scheduleJob(job, trigger);

            scheduler.start();

        } catch (Exception e) {
            String log = String.format("Não foi possível iniciar o serviço: %s", e.getMessage());
            // logger.error(log);
            System.err.println(log);
            System.exit(ERR_START);
        }

    }

    private static void loadConfigurationFile() throws IOException {

        prop = HealfiesExtractorProperties.getInstance();

    }

}
