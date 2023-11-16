package com.example.backend.configurations;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
@RestController
public class RouterController {
    @RequestMapping("/home")
    public ModelAndView redirectHome() {
        return new ModelAndView("forward:/");
    }
    @RequestMapping("/home/basic")
    public ModelAndView redirectHomeBasic() {
        return new ModelAndView("forward:/");
    }
    @RequestMapping("/home/advanced")
    public ModelAndView redirectHomeAdvanced() {
        return new ModelAndView("forward:/");
    }
    @RequestMapping("/login")
    public ModelAndView redirectLogin() {
        return new ModelAndView("forward:/");
    }
    @RequestMapping("/settings")
    public ModelAndView redirectSettings() {
        return new ModelAndView("forward:/");
    }
    @RequestMapping("/admin/user-management")
    public ModelAndView redirectUserManagement() {
        return new ModelAndView("forward:/");
    }
}