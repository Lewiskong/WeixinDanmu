package com.wechat.result;

public class GenericResult {
	private String resultState;
	
	private String resultMsg;

	public String getResultState() {
		return resultState;
	}

	public void setResultState(String resultState) {
		this.resultState = resultState;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public GenericResult(String resultState, String resultMsg) {
		super();
		this.resultState = resultState;
		this.resultMsg = resultMsg;
	}
	
	public GenericResult(){
		
	}
	
	
}
