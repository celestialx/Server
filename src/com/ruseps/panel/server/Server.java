package com.ruseps.panel.server;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.ruseps.panel.configuration.Configuration;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static Map<String, Method> serviceRead = new HashMap<>();
    public static Map<String, Method> serviceMethod = new HashMap<>();

    public static void main(String[] args) throws Exception {
        start();
    }

    public static void start() throws Exception {
        Configuration.addRoutes();
        System.out.println("Adding services");
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        for(Map.Entry<String, Method> entry : serviceRead.entrySet()) {
        	Method method = entry.getValue();
            server.createContext("/" + entry.getKey(), new ReadHandler(method, ""));
        }
        for(Map.Entry<String, Method> entry : serviceMethod.entrySet()) {
            Method method = entry.getValue();
            server.createContext("/" + entry.getKey(), new MethodHandler(method, ""));
        }
        server.setExecutor(null);
        System.out.println("Starting web server");
        server.start();
        System.out.println("Web server started");
    }


    static class ReadHandler implements HttpHandler {

    	private final Method method;
    	private String response;

    	public ReadHandler(Method method, String response) {
            this.method = method;
            this.response = response;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
        	if(t.getRemoteAddress().getAddress().getHostAddress().toString().equals(Configuration.ipv6_adress)) {
	            try {
	                Object value = method.invoke(null, t.getRequestURI().getQuery());
	                this.response = (String) value;
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            } catch (InvocationTargetException e) {
	                e.printStackTrace();
	            }
	            t.sendResponseHeaders(200, response.length());
	            try (OutputStream os = t.getResponseBody()) {
	                os.write(response.getBytes());
	            }
        	}
        }
    }

    static class MethodHandler implements HttpHandler {

    	private final Method method;
    	private String response;

    	public MethodHandler(Method method, String response) {
            this.method = method;
            this.response = response;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
        	if(t.getRemoteAddress().getAddress().getHostAddress().toString().equals(Configuration.ipv6_adress)) {
	            try {
					method.invoke(null, t.getRequestURI().getQuery());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
	            t.sendResponseHeaders(200, response.length());
	            try (OutputStream os = t.getResponseBody()) {
	                os.write(response.getBytes());
	            }
        	}
        }
    }

}