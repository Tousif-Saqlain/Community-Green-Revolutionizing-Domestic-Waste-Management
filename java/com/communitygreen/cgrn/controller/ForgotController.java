package com.communitygreen.cgrn.controller;

import com.communitygreen.cgrn.dao.UserRepository;
import com.communitygreen.cgrn.entity.User;
import com.communitygreen.cgrn.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class ForgotController {
    Random random= new Random(1000);
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/forgot")
    public String openEmailForm(){
        return "forgot_form";
    }

    @PostMapping("/send_otp")
    public String SendOTP(@RequestParam("email") String email, HttpSession session){
        System.out.println("EMAIL"+email);
        //generating otp
        int otp = random.nextInt(999999);
        System.out.println("OTP"+otp);
        String subject="Otp";
        String message=""
        +"<h3>"
        +"otp is "+otp
                +"</h3>";
        String to=email;
        boolean flag = this.emailService.sendEmail(subject, message, to);
        if (flag){
            session.setAttribute("myotp",otp);
            session.setAttribute("email",email);
            return "verify_otp";
        }else {
            session.setAttribute("message","check your emaail id");
            return "forgot_form";

        }
    }
    @PostMapping("/verify-otp")
    public String verify(@RequestParam("otp") int otp,HttpSession session){
        int myotp=(int)session.getAttribute("myotp");
        String email=(String)session.getAttribute("email");
        if (myotp==otp){
            User user = this.userRepository.getUserByUserName(email);
            if (user==null){
                session.setAttribute("message","there is no such type of user");
                return "forgot_form";
            }else {

            }
            return "change_pass";
        }else {
            session.setAttribute("message","wrong otp");
            return "verify_otp";
        }
    }
    @PostMapping("/change-password")
    public String changepass(@RequestParam("newpassword") String newpassword,HttpSession session){
        String email=(String)session.getAttribute("email");
        User user = this.userRepository.getUserByUserName(email);
        user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
        this.userRepository.save(user);
        return "redirect:/Login?change=password changed successfully";
    }

}
