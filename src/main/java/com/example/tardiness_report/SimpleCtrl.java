package com.example.tardiness_report;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SimpleCtrl {

    @RequestMapping("/search-detail")
    public String sample() {
        return "search-detail";
    }
}