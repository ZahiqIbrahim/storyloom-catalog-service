package com.example.storyloom_catalog_service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class NewHelloController {

    @Autowired
    AuthInterface authInterface;


//    @GetMapping("/helloCat")
//    public ResponseEntity<?> getHelloFromAuth(){
//        return ResponseEntity.ok(authInterface.hello());
//    }

    @PostMapping("/health")
    public ResponseEntity<?> healthCheck(HttpServletRequest request){
        try{

            return ResponseEntity.ok("health is good!");

        }catch (Exception e){
            return ResponseEntity.status(401).body(Map.of("Error",e.getMessage()));
        }

    }

}
