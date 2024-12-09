package com.dempProject.controller;

import com.dempProject.entity.ResponseStructure;
import com.dempProject.entity.User;
import com.dempProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<ResponseStructure<User>> saveUser ( @RequestBody User user){
        return userService.saveUser(user);
    }
    @DeleteMapping("/deleteUser")
    public ResponseEntity<ResponseStructure<User>> deleteUser(@RequestParam  Long id){
        return userService.deleteUser(id);
    }
    @GetMapping("/findAll")
    public ResponseEntity<ResponseStructure<Page<User>>> findAll(@RequestParam int page,
                                                                 @RequestParam int pageSize,
                                                                   @RequestParam String field){
        return  userService.findAll(page,pageSize,field);
    }
    @GetMapping("/findById")
    public ResponseEntity<ResponseStructure<User>>  findById(@RequestParam Long id){
        return userService.findById(id);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<ResponseStructure<User>> updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }
    @GetMapping("/findByEmail")
    public ResponseEntity<ResponseStructure<User>> findByEmail(@RequestParam String email){
        return userService.findByEmail(email);
    }

    @PostMapping("/loginUser")
    public ResponseEntity<ResponseStructure<User>> loginUser(@RequestParam String email , @RequestParam String password){
        return  userService.loginUser(email,password);
    }
    @PostMapping("/forgetPassword")
    public ResponseEntity<ResponseStructure<String>> forgetPassword(@RequestParam String email){
        return  userService.forgetPassword(email);
    }
    @PostMapping("/validateOtp")
    public ResponseEntity<ResponseStructure<User>> validOtp(@RequestParam Long otp){
        return userService.validotp(otp);
    }
    @PostMapping("/updatePassword")
    public ResponseEntity<ResponseStructure<User>> updatePassword(@RequestParam String  password ,@RequestParam  Long otp){
        return userService.updatePassword(password,otp);
    }
}
