package com.communitygreen.cgrn.controller;

import com.communitygreen.cgrn.Helper.message;
import com.communitygreen.cgrn.dao.UserRepository;
import com.communitygreen.cgrn.entity.User;
import jakarta.servlet.http.HttpSession;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String home()
    {
        return "home";
    }

    @RequestMapping("/Register")
    public String Register(Model model){
        model.addAttribute("user",new User());
        return "Register";
    }

    @RequestMapping(value = "/do_register",method=RequestMethod.POST)
    public String registerUser(@ModelAttribute("user")User user, Model model,HttpSession session){
//        @RequestParam(value = "agreement",defaultValue = "true") boolean agreement
        try {
//            if(!agreement){
//                throw new Exception("You have not agreed the terms and conditions");
//            }
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImgUrl("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User result=this.userRepository.save(user);
            model.addAttribute("user",new User());
            session.setAttribute("message",new message("Successfully Registered" , "alert success"));
            return "Register";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("user",user);
            session.setAttribute("message",new message("Something Went Wrong "+e.getMessage() , "alert danger"));
            return "Register";
        }

    }


    @RequestMapping("/Login")
    public String Login(){
        return "Login";
    }

    @RequestMapping("/Hiw")
    public String hiw(){
        return "hiw";
    }

    @RequestMapping("/leader")
    public String leaderboard(){
        return "leaderboard";
    }

}
