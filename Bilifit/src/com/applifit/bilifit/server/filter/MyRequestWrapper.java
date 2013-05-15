package com.applifit.bilifit.server.filter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class MyRequestWrapper extends HttpServletRequestWrapper {

	public MyRequestWrapper(HttpServletRequest request) {
		super(request);
		
		
	}
	
	public String getRequestURI(){
		
		//********************* __ clé public  __***************************************************************************************************
		
		Crypt cr = new Crypt();				
		
	      ObjectInputStream inputStream = null;
	      ObjectInputStream inputStreamprvate = null;
	      byte[] cipherText = null;
	      
		  PublicKey publicKey = null;
		  PrivateKey privateKey = null;
		  ServletContext context;
			  try {
				  inputStream = new ObjectInputStream(new FileInputStream("/Users/mohamedazaddou/Documents/workspace/Bilifit/war/WEB-INF/key/public.key"));
				  publicKey = (PublicKey) inputStream.readObject();
				  inputStreamprvate = new ObjectInputStream(new FileInputStream("/Users/mohamedazaddou/Documents/workspace/Bilifit/war/WEB-INF/key/private.key"));
				  privateKey = (PrivateKey) inputStreamprvate.readObject();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		 // cr.setUk(publicKey); 
    	  cr.setRK(privateKey);
    	 

		//************************************************************************************************************************
		
		
		String uri = super.getRequestURI();
		String uriTable[] = uri.split("/cr/");
		return "/cr/"+cr.decrypt(uriTable[1]);
	}
	
}
