package us.noop.server.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import us.noop.server.Main;

public class Database {
	
	private Connection conn;
	
	private Statement stat;
	
	private Main instance;
	
	public Database(Main instance, String dbname){
		this.instance = instance;
		try{
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbname);
			conn.setAutoCommit(false);
			stat = conn.createStatement();
			
		}catch(Exception e){
			instance.getLogger().logEx(e);
		}
	}
	
	public void addTable(String tablename, String tablestat){
		String a = "create table if not exists " + tablename + " " + tablestat;
		try {
			stat.executeUpdate(a);
		} catch (SQLException e) {
			instance.getLogger().logEx(e);
		}
	}
	
	public ResultSet getSelect(String tname, String cmd){
		try {
			return stat.executeQuery("SELECT " + cmd + " FROM " + tname + ";");
		} catch (SQLException e) {
			instance.getLogger().logEx(e);
		}
		return null;
	}
	
	public void insert(String tname, String cmd){
		try{
			stat.executeUpdate("INSERT INTO " + tname + " " + cmd);
		}catch(SQLException e){
			instance.getLogger().logEx(e);
		}
	}
	
	public Statement getStatement(){
		return stat;
	}
	
	public void update(String table, String cmd){
		try{
			stat.executeUpdate("UPDATE " + table + " set " + cmd);
		}catch(SQLException e){
			instance.getLogger().logEx(e);
		}
	}
	
	public void delete(String table, String cmd){
		try{
			stat.executeUpdate("DELETE from " + table + " " + cmd);
		}catch(SQLException e){
			instance.getLogger().logEx(e);
		}
	}
}
