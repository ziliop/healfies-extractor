package com.healfies.services.client;

import java.net.URL;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ListenerJob implements Job {


	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		System.out.println("iniciando execução do serviço");
		
		Configuration config = new Configuration();
		URL url = config.getClass().getResource("/hibernate.cfg.xml");
		SessionFactory factory = config.configure(url).buildSessionFactory();
		
		//Persistence.createEntityManagerFactory("healfies-ds");
		
		Session session = factory.openSession();
		SQLQuery q = session.createSQLQuery("SELECT * FROM test");
		
		List<Object[]> o = q.list();
		
		for (Object[] row: o){
			for(Object col: row){
				System.out.print(col + " - ");
			}
			System.out.println();
		}
		
		session.close();
		
		System.out.println("terminando execução do serviço");
		
	}

}
