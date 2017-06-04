import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Api 
{
	//config
	static protected String key = "your public key here";
	static protected String secret = "your private key here";
	static String domain = "https://api.kraken.com";
	static String nonce,signature,path,data;
	private static Double nullvalue = (Double) null;
			
			
	static void calculateSignature() 
	{
	    signature = "";
	    try 
	    {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        md.update((nonce + data).getBytes());
	        Mac mac = Mac.getInstance("HmacSHA512");
	        mac.init(new SecretKeySpec(Base64.getDecoder().decode(secret.getBytes()), "HmacSHA512"));
	        mac.update(path.getBytes());
	        signature = new String(Base64.getEncoder().encode(mac.doFinal(md.digest())));
	    } 
	    catch(Exception e) {}
	    return;
	}	
	
	static double CurrentPrice(String pair) throws InterruptedException
	{
		data = "pair="+pair;
		path = "/0/public/Ticker";
		String address = domain + path;
		    
		    
		 double price = 0;
		    
		 String answer = "";
		 HttpsURLConnection c = null;
		 JSONParser parser = new JSONParser();
		 try 
		    {
		        URL u = new URL(address); 
		        c = (HttpsURLConnection)u.openConnection();
		        c.setRequestMethod("POST");
		        c.setDoOutput(true);
		        DataOutputStream os = new DataOutputStream(c.getOutputStream());
		        os.writeBytes(data);
		        os.flush();
		        os.close();
		        BufferedReader br = null;
		        if(c.getResponseCode() >= 400) 
		        {
		            return nullvalue;
		        }
		        br = new BufferedReader(new InputStreamReader((c.getInputStream())));
		        String line;
		        while ((line = br.readLine()) != null)
		            answer += line;
		        Object obj = parser.parse(answer);
		        JSONObject jsonObject = (JSONObject) obj;
		        JSONObject temp1 = (JSONObject) jsonObject.get("result");
		        JSONObject temp2 = (JSONObject) temp1.get("XXRPZUSD");
		        JSONArray temp3 = (JSONArray) temp2.get("c");
		        price=Double.parseDouble((String) temp3.get(0));
		        //System.out.println(price);
	            
		    }  
		 	catch (Exception e)
		 	{
		 		System.out.println("Network Error.. Reconnecting..");
		 		TimeUnit.SECONDS.sleep(2);
		 		return CurrentPrice(pair);
		 	}
		    finally {
		        c.disconnect();
		    }	
		 return price;
	}
	
	static String BuyCoin(String pair,double price) throws InterruptedException
	{   
		/*
		String p=String.format ("%.8f", price);
		System.out.printf("Adding buy order @ %s \n",p);
		nonce = String.valueOf(System.currentTimeMillis());
	    data = "nonce=" + nonce + "&type=buy&ordertype=limit&volume=1&pair=" + pair + "&price="+p;
	    path = "/0/private/AddOrder";
	    calculateSignature();
	    String address = domain + path;
	    HttpsURLConnection c = null;
	    String answer = "";
	    String transaction_id=null;
	    JSONParser parser = new JSONParser();
	    
	    try 
	    {
	        URL u = new URL(address); 
	        c = (HttpsURLConnection)u.openConnection();
	        c.setRequestMethod("POST");
	        c.setRequestProperty("API-Key", key);
	        c.setRequestProperty("API-Sign", signature);
	        c.setDoOutput(true);
	        DataOutputStream os = new DataOutputStream(c.getOutputStream());
	        os.writeBytes(data);
	        os.flush();
	        os.close();
	        BufferedReader br = null;
	        if(c.getResponseCode() >= 400) 
	        {
	            System.exit(1);
	        }
	        br = new BufferedReader(new InputStreamReader((c.getInputStream())));
	        String line;
	        while ((line = br.readLine()) != null)
	            answer += line;
	        Object obj = parser.parse(answer);
	        JSONObject jsonObject = (JSONObject) obj;
	        JSONArray temp1 = (JSONArray) jsonObject.get("error");
	        if(temp1.isEmpty())
	        {	
	        	JSONObject temp2 = (JSONObject) jsonObject.get("result");
	        	transaction_id=(String) ((JSONArray) temp2.get("txid")).get(0);
	        }
	        else
	        {
	        	System.out.println(temp1.get(0));
	        	return null;
	        }
            
	    } 
	    catch (Exception x) 
	    {
	    	System.out.println("Network Error.. Reconnecting..");
	    	TimeUnit.SECONDS.sleep(5);
	    	return BuyCoin(pair,price);
	    } 
	    finally 
	    {
	        c.disconnect();
	    }
	    return transaction_id;
	    */
		System.out.println("bought "+pair+" at " +price+" ");
		return "bought "+pair+" at " +price+" ";
	}	

	
	static String SellCoin(String pair,double price) throws InterruptedException
	{
	/*
		String p=String.format ("%.8f", price);
		System.out.printf("Adding sell order @ %s \n",p);
		nonce = String.valueOf(System.currentTimeMillis());
	    data = "nonce=" + nonce + "&type=sell&ordertype=limit&volume=1&pair=" + pair + "&price="+p;
	    path = "/0/private/AddOrder";
	    calculateSignature();
	    String address = domain + path;
	    HttpsURLConnection c = null;
	    String answer = "";
	    String transaction_id=null;
	    JSONParser parser = new JSONParser();
	    
	    try 
	    {
	        URL u = new URL(address); 
	        c = (HttpsURLConnection)u.openConnection();
	        c.setRequestMethod("POST");
	        c.setRequestProperty("API-Key", key);
	        c.setRequestProperty("API-Sign", signature);
	        c.setDoOutput(true);
	        DataOutputStream os = new DataOutputStream(c.getOutputStream());
	        os.writeBytes(data);
	        os.flush();
	        os.close();
	        BufferedReader br = null;
	        if(c.getResponseCode() >= 400) 
	        {
	            System.exit(1);
	        }
	        br = new BufferedReader(new InputStreamReader((c.getInputStream())));
	        String line;
	        while ((line = br.readLine()) != null)
	            answer += line;
	        Object obj = parser.parse(answer);
	        JSONObject jsonObject = (JSONObject) obj;
	        JSONArray temp1 = (JSONArray) jsonObject.get("error");
	        if(temp1.isEmpty())
	        {	
	        	JSONObject temp2 = (JSONObject) jsonObject.get("result");
	        	transaction_id=(String) ((JSONArray) temp2.get("txid")).get(0);
	        }
	        else
	        {
	        	System.out.println(temp1.get(0));
	        	return null;
	        }
            
	    } 
	    catch (Exception x) 
	    {
	    	System.out.println("Network Error.. Reconnecting..");
	    	TimeUnit.SECONDS.sleep(5);
	    	return BuyCoin(pair,price);
	    } 
	    finally 
	    {
	        c.disconnect();
	    }
	    return transaction_id;
	*/
		System.out.println("sold "+pair+" at " +price+" ");
		return "sold "+pair+" at " +price+" ";
	}

	

}