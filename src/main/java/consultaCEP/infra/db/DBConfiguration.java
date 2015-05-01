package main.java.consultaCEP.infra.db;

public class DBConfiguration {

	public static String getDbConnectionString(){
		String connectionString = System.getenv("DB_Connection_String");
		if (connectionString == null || connectionString.isEmpty()) {
			connectionString = "mongodb://" + getDbUser() + ":" + getDbPassword() + "@ds053139.mongolab.com:53139/consultacep_dev";
		}		
		return connectionString;
	}
	
	public static String getDbUser(){
		String user = System.getenv("DB_User");
		if (user == null || user.isEmpty()) {
			user = "usr_consultacep";
		}		
		return user;
	}
	
	public static String getDbPassword(){
		String password = System.getenv("DB_Password");
		if (password == null || password.isEmpty()) {
			password = "F3n1x";
		}		
		return password;
	}
	
	public static String getDbName(){
		String name = System.getenv("DB_Name");
		if (name == null || name.isEmpty()) {
			name = "consultacep_dev";
		}		
		return name;
	}
	
	/*
	 * 	private String dbname = "consultacep_dev";
	private String user = "usr_consultacep";
	private String password = "F3n1x";
	private String connectionString = "mongodb://" + user + ":" + password
			+ "@ds053139.mongolab.com:53139/consultacep_dev";
	 */
	
}
