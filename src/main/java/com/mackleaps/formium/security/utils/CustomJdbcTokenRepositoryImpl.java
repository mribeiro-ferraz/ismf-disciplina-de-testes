package com.mackleaps.formium.security.utils;

import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

/**
 * i'm going to think about this. It does not seem to be right, but at least 
 * provides a solution to a couple of problems with the default option
 * 
 * This is for debug purposes only, for it won't throw an exception because a table was already created with that name.
 * JdbcTokenRepositoryImpl will make an attempt on creating a new table that it will use to control tokens
 * The current "version" make a small change on the table creation query in order to avoid such problems
 * */
public class CustomJdbcTokenRepositoryImpl extends JdbcTokenRepositoryImpl {

	public static final String CREATE_TABLE_SQL = "create table if not exists persistent_logins (username varchar(64) not null, series varchar(64) primary key, "
+ "token varchar(64) not null, last_used timestamp not null)";
	
	private boolean createTableOnStartup;

	@Override
	protected void initDao() {
		if(createTableOnStartup)
			getJdbcTemplate().execute(CREATE_TABLE_SQL);
	}
	
	@Override
	public void setCreateTableOnStartup(boolean createTableOnStartup) {
		super.setCreateTableOnStartup(createTableOnStartup);
		this.createTableOnStartup = createTableOnStartup;
	}
	
}
