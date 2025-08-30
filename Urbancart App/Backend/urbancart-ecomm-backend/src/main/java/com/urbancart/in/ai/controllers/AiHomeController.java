package com.urbancart.in.ai.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbancart.in.response.ApiResponse;

//import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiHomeController {

    @GetMapping()
    public ResponseEntity<ApiResponse> AiHome(){
        ApiResponse response = new ApiResponse();
        response.setMessage("welcome to ai world");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
