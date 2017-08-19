package com.yonyou;

public class TClass {
	private String tableName;
	private String tableDesc;
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
	@Override
	public String toString() {
		return "TClass [tableName=" + tableName + ", tableDesc=" + tableDesc + "]";
	}
	
	

}
