package com.applifit.bilifit.server.filter;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;

public class Crypt {
	public static PublicKey uk;
    public static PrivateKey rk;
	
	
	
    public Crypt() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
    
    
//    public static byte[] encrypt(String text, PublicKey key) {
//	    byte[] cipherText = null;
//	    try {
//	      // get an RSA cipher object and print the provider
//	      final Cipher cipher = Cipher.getInstance("RSA");
//	      // encrypt the plain text using the public key
//	      cipher.init(Cipher.ENCRYPT_MODE, key);
//	      cipherText = cipher.doFinal(text.getBytes());
//	    } catch (Exception e) {
//	      e.printStackTrace();
//	    }
//	    return cipherText;
//	  }

	// _______ ******************------------_______________----------_________------------_________
    
 public void setUk(PublicKey uk) {
		Crypt.uk = uk;
	}
 
 public void setRK(PrivateKey rk) {
		Crypt.rk = rk;
	}

public void generateKey() throws Exception
    {
     KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA"); 
     gen.initialize(512, new SecureRandom()); 
     KeyPair keyPair = gen.generateKeyPair(); 
     uk = keyPair.getPublic();
     rk = keyPair.getPrivate();
    }
 
  private byte[] encrypt(String text, PublicKey pubRSA) throws Exception
    {
     Cipher cipher = Cipher.getInstance("RSA"); 
     cipher.init(Cipher.ENCRYPT_MODE, pubRSA);
     return cipher.doFinal(text.getBytes());
    }
    public String encrypt(String text)
    {
     try {
      return byte2hex(encrypt(text, uk));
     }
     catch(Exception e)
     {
      e.printStackTrace();
     }
     return null;
    }
    
    public String decrypt(String data)
    {
     try{
      return new String(decrypt(hex2byte(data.getBytes())));
     }
     catch (Exception e)
     {
      e.printStackTrace();
     }
     return null;
    }
    
    private byte[] decrypt(byte[] src) throws Exception
    {
     Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
     cipher.init(Cipher.DECRYPT_MODE, rk);
     return cipher.doFinal(src);
    } 
    
    public String byte2hex(byte[] b)
    {
     String hs = "";
     String stmp = "";
     for (int n = 0; n < b.length; n ++)
     {
      stmp = Integer.toHexString(b[n] & 0xFF);
      if (stmp.length() == 1)
       hs += ("0" + stmp);
      else
       hs += stmp;
     }
     return hs.toUpperCase();
    }
    
    public  byte[] hex2byte(byte[] b)
    {
     if ((b.length % 2) != 0)
      throw new IllegalArgumentException("hello");
     
     byte[] b2 = new byte[b.length / 2];
     
     for (int n = 0; n < b.length; n += 2)
     {
      String item = new String(b, n, 2);
      b2[n/2] = (byte)Integer.parseInt(item, 16);
     }
     return b2;
    }
    
    

}
