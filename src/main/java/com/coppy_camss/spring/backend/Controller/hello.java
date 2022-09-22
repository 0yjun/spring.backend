package com.coppy_camss.spring.backend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hello {
    @GetMapping("hello/{name}")
    public helloBean hello(@PathVariable String name){
        return new helloBean(String.format("hello %s",name));
    }
}
