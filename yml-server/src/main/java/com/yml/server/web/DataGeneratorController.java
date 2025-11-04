package com.yml.server.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DataGeneratorController {

    @GetMapping("/blbb/data-generator")
    public String dataGenerator() {
        return "data-generator";
    }
}