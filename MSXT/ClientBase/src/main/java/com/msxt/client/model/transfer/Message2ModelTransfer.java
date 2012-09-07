package com.msxt.client.model.transfer;

import org.w3c.dom.Element;

import com.msxt.client.model.Examination;
import com.msxt.client.model.LoginSuccessResult;
import com.msxt.client.model.SubmitSuccessResult;

public interface Message2ModelTransfer {
	public LoginSuccessResult parseResult( Element root );
	public Examination parseExamination( Element examination );
	public SubmitSuccessResult parseSubmitResult( Element root );
	
	public static class Factory{
		private static Message2ModelTransfer instance; 
		
		public static Message2ModelTransfer getInstance(){
			if( instance == null )
				instance = new Message2ModelTransferImpl();
			return instance;
		}
	}
}
