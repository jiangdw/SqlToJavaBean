package com.yonyou;

public class DBFiled {
	private String varType;//变量类型
	private String varName;//变量名字
	private String varDesc;//变量注释
	public String getVarType() {
		return varType;
	}
	public void setVarType(String varType) {
		this.varType = varType;
	}
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	public String getVarDesc() {
		return varDesc;
	}
	public void setVarDesc(String varDesc) {
		this.varDesc = varDesc;
	}
	@Override
	public String toString() {
		return "VarBean [varType=" + varType + ", varName=" + varName + ", varDesc=" + varDesc + "]";
	}
	
	
	
}
