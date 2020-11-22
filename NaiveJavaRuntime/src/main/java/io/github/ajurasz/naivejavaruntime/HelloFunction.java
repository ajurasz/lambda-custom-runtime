package io.github.ajurasz.naivejavaruntime;

class HelloFunction {
    String handle(String event) {
        System.out.println("Event: " + event);
        return "Hello!";
    }
}
