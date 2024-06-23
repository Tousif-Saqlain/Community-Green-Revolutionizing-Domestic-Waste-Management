package com.communitygreen.cgrn.controller;

import com.communitygreen.cgrn.Helper.message;
import com.communitygreen.cgrn.dao.HistryRepository;
import com.communitygreen.cgrn.dao.UserRepository;
import com.communitygreen.cgrn.entity.Histry;
import com.communitygreen.cgrn.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;
import deepnetts.net.ConvolutionalNetwork;
import deepnetts.util.FileIO;
import com.communitygreen.cgrn.Ml.dl4j.GarOrNotDl4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.visrec.ml.classification.ImageClassifier;
import javax.visrec.ri.ml.classification.ImageClassifierNetwork;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistryRepository histryRepository;
//    private void userDetails(Model m, Principal p){
//        String email=p.getName();
//
//        User user=userRepository.getUserByUserName(email);
//        m.addAttribute("user",user);
//    }
//

    @ModelAttribute
    public void addCommonData(Model model,Principal principal){
        String userName = principal.getName();
        System.out.println("USERNAME"+userName);
        User user = userRepository.getUserByUserName(userName);
        System.out.println("USERD "+user);
        model.addAttribute("user",user);
    }

    @GetMapping("/public")
    public String userprofile(Model model,Principal principal){
        return "User/userprofile";
    }

    //open profile
    @GetMapping("/addImage")
    public String openAddImage(Model model)
    {
        model.addAttribute("histry",new Histry());
        return "User/addup";
    }

    //process add image
    @PostMapping("/process-image")
    public String processImage(
            @ModelAttribute Histry histry,
            @RequestParam("processimage") MultipartFile file,
            Principal principal, HttpSession session){

        try {
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);
            //processing file
            if (file.isEmpty()){
                //if file is empty then try our msg
                System.out.println("File is empty");
            }else {
                histry.setImage(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image is uploaded");
            }

            user.getHistories().add(histry);
            histry.setUser(user);

            this.userRepository.save(user);
            System.out.println("DATA"+histry);
            System.out.println("Added Data");
            //msg sucess...
            session.setAttribute("message",new message("Successfully Send","success"));
            final Logger LOGGER = LoggerFactory.getLogger(GarOrNotDl4j.class);
            ConvolutionalNetwork convNet =  FileIO.createFromFile("hotdog.dnet", ConvolutionalNetwork.class);
            ImageClassifier<BufferedImage> classifier = new ImageClassifierNetwork(convNet);
            BufferedImage image=ImageIO.read(new ClassPathResource("static/img/nbi2.jpeg").getFile());
            Map<String, Float> results = classifier.classify(image);
            float hotDogProbability = results.get("hotdog");
            if (hotDogProbability > 0.5) {
                LOGGER.info("There is a high probability that this is non a biodegradable");
                session.setAttribute("message",new message("There is a high probability that this is non biodegradable","success"));

            } else {
                LOGGER.info("Most likely this is biodegradable");
                session.setAttribute("message",new message("There is a high probability that this is a biodegradable","success"));
            }

        }catch (Exception e){
            System.out.println("Error "+e.getMessage());
            e.printStackTrace();
            //error msg...
            session.setAttribute("message",new message("Something went wrong, Try Again","danger"));
        }

        return "User/ml";
    }

    //Show History
//    pagination
//    per page =10[n]
//    current page=0[page]
    @GetMapping("/show-histry/{page}")
    public String showHistry(@PathVariable("page") Integer page,Model m,Principal principal){
        //send data to frntend
        //[this is used to return all histry data of every user
//        this.histryRepository.findAll();]
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);
        Pageable pageable = PageRequest.of(page, 3);
        Page<Histry> histries = this.histryRepository.findHistriesByUser(user.getId(),pageable);
        m.addAttribute("histries",histries);
        m.addAttribute("currentPage",page);
        m.addAttribute("totalpages",histries.getTotalPages());
        return "User/profile";
    }

    //showing particular image details
    @RequestMapping("/{iID}/view")
    public String showImageDetail(@PathVariable("iID")Integer iID,Model model,Principal principal){
        System.out.println("CID"+iID);
        Optional<Histry> view = this.histryRepository.findById(iID);
        Histry histry = view.get();
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);
        if(user.getId()==histry.getUser().getId())
        model.addAttribute("histry",histry);
        return "User/imageDetails";
    }

    //delete
    @GetMapping("/delete/{iID}")
    public String deleteDetails(@PathVariable("iID")Integer iID,Model model,HttpSession session,Principal principal){
        Histry histry = this.histryRepository.findById(iID).get();
        //check.A image delete
        User user = this.userRepository.getUserByUserName(principal.getName());
        user.getHistories().remove(histry);
        this.userRepository.save(user);
        session.setAttribute("message",new message("deleted succesfully","success"));
        return "redirect:/user/show-histry/0";
    }

    //update form
    @PostMapping("/update-details/{iID}")
    public String updateDetails(@PathVariable("iID")Integer iID,Model m){
        m.addAttribute("title","update details");
        Histry histry = this.histryRepository.findById(iID).get();
        m.addAttribute("histry",histry);
        return "User/updateaddup";
    }

    //real update
    @RequestMapping(value = "/process-update",method = RequestMethod.POST)
    public String updateHandler(@ModelAttribute Histry histry,@RequestParam("processimage") MultipartFile file,Model m,
                                HttpSession session,Principal principal)
    {
        try {
           Histry oldDetails= this.histryRepository.findById(histry.getiID()).get();
            if (!file.isEmpty()){

                //delete old
                File deleteFile = new ClassPathResource("static/img").getFile();
                File file1=new File(deleteFile,oldDetails.getImage());
                file1.delete();

                //update new
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                histry.setImage(file.getOriginalFilename());

            }else{
                histry.setImage(oldDetails.getImage());
            }
            User user = this.userRepository.getUserByUserName(principal.getName());
            histry.setUser(user);
            this.histryRepository.save(histry);
            session.setAttribute("message",new message("Updated","success"));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Histry Add"+histry.getAddress());
        System.out.println("Histry ID"+histry.getiID());
        return "redirect:/user/"+histry.getiID()+"/viewa";
    }
//profile
    @GetMapping("/profile")
    public String yourProfile( Model m){
        return "Admin/profile";
    }
    @GetMapping("Uprofile")
    public String Uprofile(Model m){
        return "User/profile";
    }
//----------------------------ADMIN---------------------------------------------

    @GetMapping("/admin")
    public String adminprofile(){
        return "Admin/adminprofile";
    }

    @GetMapping("/show-histrya/{page}")
    public String showHistrya(@PathVariable("page") Integer page,Model m,Principal principal){
        //send data to frntend
        //[this is used to return all histry data of every user
        String userName = principal.getName();
/*
        if(history.user === list.user){
            map.count += 1;
        }else{
        map.enter(user,1)
        }
        * */

//        User user = this.userRepository.getUserByUserName(userName);
        Pageable pageable = PageRequest.of(page, 3);
        Page<Histry> all = this.histryRepository.findAll(pageable);
//        Page<Histry> histries = this.histryRepository.findHistriesByUser(user.getId(),pageable);
        m.addAttribute("histries",all);
        m.addAttribute("currentPage",page);
        m.addAttribute("totalpages",all.getTotalPages());
        return "Admin/admindash";
    }
    @GetMapping("/{iID}/viewa")
    public String showImageDetaila(@PathVariable("iID")Integer iID,Model model,Principal principal){
        System.out.println("CID"+iID);
        Optional<Histry> view = this.histryRepository.findById(iID);
        Histry histry = view.get();
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);
            model.addAttribute("histry",histry);
        return "Admin/imgdetail";
    }

    @RequestMapping("/Hiw")
    public String hiw(){
        return "hiw";
    }

}
