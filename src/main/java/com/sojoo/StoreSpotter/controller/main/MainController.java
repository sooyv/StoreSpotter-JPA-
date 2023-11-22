package com.sojoo.StoreSpotter.controller.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {

    @GetMapping("/")
    public ModelAndView index() {

        return new ModelAndView("index/index");
    }

    @PostMapping("/api/industry")
    public void chooseIndust(@RequestBody String indust_id) {
        System.out.println(indust_id);
    }
}
