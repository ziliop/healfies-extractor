package com.healfies.services.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.imageio.stream.FileCacheImageInputStream;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.healfies.services.api.rest.dto.CommandData;

public class ListenerJob implements Job {


	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		System.out.println("iniciando execução do serviço");
		
		
		String token = getToken();
		
		CommandData commandData = getCommand();
		
		Configuration config = new Configuration();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(commandData.getConfigFileContents().getBytes());
	

			SessionFactory factory = config.addInputStream(bais).configure().buildSessionFactory();
			
			//Persistence.createEntityManagerFactory("healfies-ds");
			
			Session session = factory.openSession();
			SQLQuery q = session.createSQLQuery(commandData.getSqlCommand());
			
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

	private String getToken() {
		
		RestTemplate rest = new RestTemplate();
		
		//rest.postForEntity("localhost:8080/", request, responseType)
		
		return null;
	}
	
	private CommandData getCommand(){
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		RestTemplate rest = new RestTemplate();

		ResponseEntity<CommandData> response = rest.getForEntity("http://localhost:8080/healfies-services-api-rest/api/services/command", CommandData.class);
		
		return response.getBody();
	}

}
