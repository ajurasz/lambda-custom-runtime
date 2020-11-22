package io.github.ajurasz.naivejavaruntime;

import io.quarkus.funqy.Funq;

public class HelloFunction {
    @Funq
    public String handle(String event) {
        System.out.println("Event: " + event);
        return "Hello!";
    }
}
