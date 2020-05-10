package ru.nigmatzianov.shoppingListBot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class TestController {
    private static final String template = "Hello, %s";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public HashMap<String, String> greeting() {
        HashMap<String, String> greeting = new HashMap<>();
        greeting.put("test", "Hello Ilnar");

        return greeting;
    }
}
