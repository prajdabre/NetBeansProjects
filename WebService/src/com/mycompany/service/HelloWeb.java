package com.mycompany.service;

import javax.jws.WebMethod;

import javax.jws.WebService;

@WebService
public class HelloWeb {

    @WebMethod
    public String sayGreeting(String name) {

        return "Greeting " + name + "!";

    }
}