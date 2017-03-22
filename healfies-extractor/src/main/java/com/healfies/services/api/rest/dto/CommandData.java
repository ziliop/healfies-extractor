package com.healfies.services.api.rest.dto;

public class CommandData {
	
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
