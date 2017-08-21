package com.yonyou;

import java.util.Set;

public class TClass {
	private String tableName;
	private String tableDesc;
	private Set<DBFiled> DBFiledSet;
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableDesc() {
		return tableDesc;
	}
	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
	public Set<DBFiled> getDBFiledSet() {
		return DBFiledSet;
	}
	public void setDBFiledSet(Set<DBFiled> dBFiledSet) {
		DBFiledSet = dBFiledSet;
	}
	
	@Override
	public String toString() {
		return "TClass [tableName=" + tableName + ", tableDesc=" + tableDesc + ", DBFiledSet=" + DBFiledSet.size() + "]";
	}
	
	
	
	

}
