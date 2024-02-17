package com.example;

import jakarta.inject.Singleton;

@Singleton
public class MessageService {
    public String sayHello() {
        return "Hello from MessageService";
    }

    public String sayHello2() {
        return "Hello from MessageService2";
    }
}
