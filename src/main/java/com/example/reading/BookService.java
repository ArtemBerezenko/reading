package com.example.reading;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class BookService {

    private final RestTemplate restTemplate;

    public BookService(RestTemplate rest) {
        this.restTemplate = rest;
    }

    @HystrixCommand(
            fallbackMethod = "reliable",
            commandProperties = {
                    @HystrixProperty(
                            name="circuitBreaker.requestVolumeThreshold",
                            value="30"),
                    @HystrixProperty(
                            name="circuitBreaker.errorThresholdPercentage",
                            value="25"),
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "500"),
                    @HystrixProperty(
                            name="circuitBreaker.sleepWindowInMilliseconds",
                            value="60000")
            })
    public String readingList() {
        URI uri = URI.create("http://localhost:8090/recommended");

        return this.restTemplate.getForObject(uri, String.class);
    }

    public String reliable() {
        return "Cloud Native Java (O'Reilly)";
    }

}
