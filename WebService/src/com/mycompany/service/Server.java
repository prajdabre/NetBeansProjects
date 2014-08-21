package com.mycompany.service;
 
import javax.xml.ws.Endpoint;
 
public class Server {
 
    public static void main(String[] args) {
 
        Endpoint.publish("http://localhost:9898/HelloWeb", new HelloWeb());
 
        System.out.println("HelloWeb service is ready");
 
    }
 
}