package com.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Chenzhou Li";
    }

    @RequestMapping("/hello")
    public String sayHello(@RequestParam(required = true,defaultValue = "张三") String name){
        return "hello"+name;
    }

    @RequestMapping("/hid/{id}")
    public String hid(@PathVariable int id){
        String name;
        switch (id){
            case 1:
                name="joker";
                break;
            case 2:
                name="panther";
                break;
            case 3:
                name="navi";
                break;
            default:
                name="skull";
        }
        return "hi," + name;
    }

}