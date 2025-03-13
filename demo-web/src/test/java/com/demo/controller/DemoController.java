package com.demo.controller;
 
import org.springframework.web.bind.annotation.*;
 
@RestController

@RequestMapping("/movies")

public class DemoController {
 
    // GET request to accept firstName and lastName as RequestParams

    @GetMapping("/name")

    public String getFullName(@RequestParam String firstName, @RequestParam String lastName) {

        return "Full Name: " + firstName + " " + lastName;

    }
 
    // GET request using PathVariables to calculate total price

    @GetMapping("/price/{price}/{quantity}")

    public String getTotalPrice(@PathVariable double price, @PathVariable int quantity) {

        double total = price * quantity;

        return "Total Price: $" + total;

    }
 
    // POST request to add a new movie

    @PostMapping("/add")

    public String addMovie(@RequestParam String title, @RequestParam String director) {

        return "Movie Added: " + title + " directed by " + director;

    }

}
 