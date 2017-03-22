package com.healfies.services.api.rest.dto;

import java.io.Serializable;

public class CommandData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4358298235221172159L;
	
	private String configFileContents;
	private String sqlCommand;
	public String getConfigFileContents() {
		return configFileContents;
	}
	public void setConfigFileContents(String configFileContents) {
		this.configFileContents = configFileContents;
	}
	public String getSqlCommand() {
		return sqlCommand;
	}
	public void setSqlCommand(String sqlCommand) {
		this.sqlCommand = sqlCommand;
	}

}
