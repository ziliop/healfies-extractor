package com.healfies.services.client;

import java.io.IOException;
import java.util.Properties;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class Main {
	
	private static final int ERR_START = 7;
	
	private static Properties prop = new Properties();

	public static void main(String[] args) throws IOException {
		
		// check configuration file and make sure it's correct
        loadConfigurationFile();

        // everything is ready, start the server
        verificarEIniciar();

	}

	private static void verificarEIniciar() {
		

	        //logger.trace("Starting the Openstack event collector micro service");
	        try {
	        	
	            // also start collection immediately
	            Integer time = Integer.valueOf(prop.getProperty("com.healfies.extractor.scheduler"));
	            
	            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
	            
	            JobDetail job = JobBuilder.newJob(ListenerJob.class).withIdentity("job1", "group1").build();
	            
	            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule()
	                    .withIntervalInSeconds(time)
	                    .repeatForever())            
	            .build();
	            scheduler.scheduleJob(job, trigger);
	            
	            scheduler.start();
	            
	        } catch (Exception e) {
	            String log = String.format("Não foi possível iniciar o serviço: %s", e.getMessage());
	            //logger.error(log);
	            System.err.println(log);
	            System.exit(ERR_START);
	        }
		
	}

	private static void loadConfigurationFile() throws IOException {
		
		prop.load(prop.getClass().getResourceAsStream("/healfies-extractor.properties"));
		
	}

}
