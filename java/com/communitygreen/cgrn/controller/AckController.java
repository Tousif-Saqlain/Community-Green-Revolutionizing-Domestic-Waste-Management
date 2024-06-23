package com.communitygreen.cgrn.controller;

import com.communitygreen.cgrn.dao.HistryRepository;
import com.communitygreen.cgrn.dao.UserRepository;
import com.communitygreen.cgrn.entity.Histry;
import com.communitygreen.cgrn.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AckController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HistryRepository histryRepository;

    @PostMapping("/send_ack")
    public String SendOTP(@RequestParam("email") String email, HttpSession session){
        System.out.println("EMAIL"+email);
        //sending
        String subject="Confirmation of Your Cleaning Request and Bonus Approval";
        String message=""
                +"<h3>"
                +"Dear Sir/Mam"
                +"</h3>"+
                "I hope this message finds you well."
                +"I am pleased to inform you that your request for cleaning has been "+
                "<h3>"+
                "Accepted."+"</h3>" +
                "Our team will ensure that the cleaning is carried out to the highest standards and completed in a timely manner" +
                "Additionally, we are happy to inform you that you will receive a bonus for generating this request. We greatly appreciate your proactive approach in maintaining the cleanliness and hygiene of our environment.\n" +
                "Thank you once again for your contribution.\n" +
                "\n"
                +"<h3>"
                + "Best regards"
                + "</h3>"
                +"<h4>"+"CommunityGreen"+"</h4>";
        String to=email;
        boolean flag = this.emailService.sendEmail(subject, message, to);
        return "Admin/success";
    }
    @PostMapping("/send_ack1")
    public String SendOTP1(@RequestParam("email") String email, HttpSession session){
        System.out.println("EMAIL"+email);
        //sending
        String subject="Confirmation of Your Cleaning Request and Bonus Approval";
        String message=""
                +"<h3>"
                +"Dear Sir/Mam"
                +"</h3>"+
                "I hope this message finds you well."
                +"I am regret to inform you that your request for cleaning has been "+
                "<h3>"+
                "Rejected."+"</h3>" +
                "Our team will ensure that the cleaning is carried out to the highest standards and completed in a timely manner" +
                "Additionally, we are happy to inform you that you will receive a bonus for generating this request. We greatly appreciate your proactive approach in maintaining the cleanliness and hygiene of our environment.\n" +
                "Thank you once again for your contribution.\n" +
                "\n"
                +"<h3>"
                + "Best regards"
                +" CommunityGreen"
                + "</h3>";
        String to=email;
        boolean flag = this.emailService.sendEmail(subject, message, to);
        return "Admin/reject";
    }
}
