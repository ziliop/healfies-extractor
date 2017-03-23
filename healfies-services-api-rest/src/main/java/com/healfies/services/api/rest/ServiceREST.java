package com.healfies.services.api.rest;

import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.healfies.services.api.dto.UserData;
import com.healfies.services.api.rest.dto.CommandData;
import com.healfies.services.api.rest.dto.ResultsData;

@Path("services")
public class ServiceREST {

	
	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String login(UserData user){
		
		return "sdfsdfsdf sdf sdfsdfsdfsdf";
		
	}
	
	@Path("command")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public CommandData getExecutionCommand(@QueryParam("clientID") String clientID){
		CommandData cd = new CommandData();
		try {
			InputStream bais = this.getClass().getResourceAsStream("/hibernate.cfg.xml");
			InputStreamReader reader = new InputStreamReader(bais);
			BufferedReader br = new BufferedReader(reader);
			
			StringBuffer sb = new StringBuffer();
			
			while (br.ready()){
				sb.append(br.readLine());
			}
				
			cd.setConfigFileContents(sb.toString());
			cd.setSqlCommand("SELECT * FROM test");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cd;
	}
	
	@POST
	@Path("results")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postResults(ResultsData results){
		return Response.ok().build();
	}
	
}
