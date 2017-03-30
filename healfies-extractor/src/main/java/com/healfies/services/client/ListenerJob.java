package com.healfies.services.client;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.transform.stream.StreamSource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.cfgxml.spi.LoadedConfig;
import org.hibernate.boot.jaxb.cfg.spi.JaxbCfgHibernateConfiguration;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healfies.services.api.rest.dto.CommandData;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;

public class ListenerJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {

        System.out.println("iniciando execuçãoo do serviço");

        String token = getToken();

        CommandData commandData = getCommand();

        Configuration config = new Configuration();

        String xmlStr = commandData.getConfigFileContents();

        try {
            JAXBContext jaxb = JAXBContextImpl.newInstance(JaxbCfgHibernateConfiguration.class);

            JaxbCfgHibernateConfiguration jaxbCfg = (JaxbCfgHibernateConfiguration) jaxb.createUnmarshaller()
                .unmarshal(new StreamSource(new StringReader(xmlStr.toString())));

            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();

            LoadedConfig loadedConfig = LoadedConfig.consume(jaxbCfg);
            StandardServiceRegistry svc = builder.configure(loadedConfig).build();
            SessionFactory factory = config.buildSessionFactory(svc);

            // Persistence.createEntityManagerFactory("healfies-ds");

            Session session = factory.openSession();
            SQLQuery q = session.createSQLQuery(commandData.getSqlCommand());

            List<Object[]> o = q.list();

            for (Object[] row : o) {
                for (Object col : row) {
                    System.out.print(col + " - ");
                }
                System.out.println();
            }

            session.close();

            System.out.println("terminando execução do serviço");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private String getToken() {

        RestTemplate rest = new RestTemplate();

        // rest.postForEntity("localhost:8080/", request, responseType)

        return null;
    }

    private CommandData getCommand() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>("", headers);

        RestTemplate rest = new RestTemplate();

        ResponseEntity<String> response = rest.getForEntity(
            "http://localhost:8080/healfies-services-api-rest/api/services/command", String.class);

        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(response.getBody().getBytes(), CommandData.class);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new CommandData();
    }

}
