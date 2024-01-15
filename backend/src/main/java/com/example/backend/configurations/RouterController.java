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
    @RequestMapping("/login/{entry-code}")
    public ModelAndView redirectLoginWithEntryCode() {
        return new ModelAndView("forward:/");
    }
    @RequestMapping("/register")
    public ModelAndView redirectRegister() {
        return new ModelAndView("forward:/");
    }
    @RequestMapping("/settings")
    public ModelAndView redirectSettings() {
        return new ModelAndView("forward:/");
    }
    @RequestMapping("/admin")
    public ModelAndView redirectAdmin() {
        return new ModelAndView("forward:/");
    }
    @RequestMapping("/admin/roles")
    public ModelAndView redirectRoles() {
        return new ModelAndView("forward:/");
    }
    @RequestMapping("/admin/roles/display/{role-id}")
    public ModelAndView redirectRoleWithId() {
        return new ModelAndView("forward:/");
    }
    @RequestMapping("/admin/roles/members/{role-id}")
    public ModelAndView redirectRoleMembersWithId() {
        return new ModelAndView("forward:/");
    }
    @RequestMapping("/admin/roles/permissions/{role-id}")
    public ModelAndView redirectRolePermissionsWithId() {
        return new ModelAndView("forward:/");
    }
    @RequestMapping("/admin/users")
    public ModelAndView redirectUsers() {
        return new ModelAndView("forward:/");
    }
}