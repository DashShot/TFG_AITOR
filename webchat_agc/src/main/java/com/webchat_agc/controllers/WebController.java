package com.webchat_agc.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.webchat_agc.dto.User;
import com.webchat_agc.dto.UserStatus;
import com.webchat_agc.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class WebController {
    
    private  UserService userService;

    private  PasswordEncoder passwordEncoder;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

            model.addAttribute("sesionIniciada", true);
            model.addAttribute("userName", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));

        } else {
            model.addAttribute("sesionIniciada", false);
            model.addAttribute("userName", "Invitado");
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
        }

    }

    // --------------- Pantalla inicial ----------------------------------//

    @GetMapping("/")
    public String inicio() {


        return "inicio";
    }

    // ------------------------ Login ----------------------------------------//
    
    @RequestMapping("/login")
    public String login() {
        return "login";
    }


    @RequestMapping("/loginerror")
    public String loginerror() {
        return "loginerror";
    }

    //----------------------- Logout --------------------------------------//

    @RequestMapping("/logout")
    public String logout(){
        return "logout";
    }
    // ------------------ Registro -------------------------------------//

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @PostMapping("/registro")
	public String registrar(@RequestParam String nickName, @RequestParam String password) {

		if (userService.getByNickname(nickName) != null || nickName == "" || password == ""){
            return "registroerror";
        }else{
            User u = new User(nickName,passwordEncoder.encode(password),("USER"));
		    userService.saveUser(u);
		    return "registroexito";
        }
        
	}
}
