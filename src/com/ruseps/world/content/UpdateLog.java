package com.ruseps.world.content;

import java.net.*;
import java.io.*;

public class UpdateLog {

	private static boolean fetchLogs = true;
	
	public static void sequence() {
		if(fetchLogs) {
			try {
				getUpdatelog();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	    public static void getUpdatelog() throws Exception {

	        URL web = new URL("https://www.CelestialX.org/updateLog.txt");
	        BufferedReader in = new BufferedReader(
	        new InputStreamReader(web.openStream()));

	        String inputLine;
	        while ((inputLine = in.readLine()) != null)
	         //   System.out.println(inputLine);
	        in.close();
	       // System.out.println("[WEBSITE] Loaded update log!");
	    }
	}
	
