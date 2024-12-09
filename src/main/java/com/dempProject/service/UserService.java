package com.dempProject.service;

import com.dempProject.dao.UserDao;
import com.dempProject.entity.ResponseStructure;
import com.dempProject.entity.User;
import com.dempProject.utill.Aes;
import com.dempProject.utill.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private EmailSender emailSender;


    public ResponseEntity<ResponseStructure<User>> saveUser(User user){
        ResponseStructure<User> responseStructure=new ResponseStructure<>();
        String encryptedPassword = Aes.encrypt(user.getPassword());
        user.setPassword(encryptedPassword);
        userDao.saveUser(user);
        responseStructure.setStatus(HttpStatus.CREATED.value());
        responseStructure.setMessage("User saved successfully");
        responseStructure.setData(user);
        return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseStructure<User>>  deleteUser(Long id){
        ResponseStructure<User> responseStructure =new ResponseStructure<>();
        User user =  userDao.findById(id);
        if(user ==null){
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User not found");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        }else{
            userDao.deleteUser(user);
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("User deleted successfully");
            responseStructure.setData(user);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }
    public ResponseEntity<ResponseStructure<User>> updateUser(User user) {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        User user1 =userDao.findById(user.getId());
        if(user1 != null){
            user.setPassword(Aes.encrypt(user.getPassword()));
            userDao.updateUser(user);
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("User updated successfully");
            responseStructure.setData(user);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }else{
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User not found");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ResponseStructure<Page<User>>> findAll(int page, int pageSize, String field){
        ResponseStructure<Page<User>> responseStructure = new ResponseStructure<>();
        Page<User> mobile = userDao.findAll(page, pageSize, field);
        if (mobile == null){
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("Mobile not found");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        }else{
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("All mobile Founds");
            responseStructure.setData(mobile);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseStructure<User>> findById(Long id){
        ResponseStructure<User> responseStructure=new ResponseStructure<>();
        User user = userDao.findById(id);
        if(user == null){
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User not found");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        }else{
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("User found");
            responseStructure.setData(user);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseStructure<User>> findByEmail(String email){
        ResponseStructure<User> responseStructure=new ResponseStructure<>();
        User user =userDao.findByEmail(email);
        if(user == null){
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User not found");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        }else{
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("User found by email");
            responseStructure.setData(user);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseStructure<User>> loginUser(String email ,String password){
        ResponseStructure<User> responseStructure=new ResponseStructure<>();
        User user =userDao.findByEmail(email);
        if(user == null){
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User not found with email" +email);
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        }
        if(password == null){
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("password not found with email" +email);
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure,HttpStatus.NOT_FOUND);
        }
        if(!Aes.decrypt(user.getPassword()).equals(password)){
            responseStructure.setStatus(HttpStatus.UNAUTHORIZED.value());
            responseStructure.setMessage("Password is incorrect for user" +email);
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.UNAUTHORIZED);
        }else{
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("User logged in successfully");
            responseStructure.setData(user);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseStructure<String>>  forgetPassword(String email){
        ResponseStructure<String> responseStructure=new ResponseStructure<>();
        User user = userDao.findByEmail(email);
        if(user == null){
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("email does not exists" +email);
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        }else{
            Long otp = (long) (Math.random() * (9999 - 1000) + 1000);
            user.setOtp(otp);
            userDao.saveUser(user);
            emailSender.sendEmail(user.getEmail(), "This is Your OTP \n" +
                            " Don't Share OTP with Anyone\n " +
                            "Enter this OTP To Update Password \n" + " -> OTP " ,
                    "Your OTP To Update Password" +otp);

            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("OTP sent to email ID:"+email);
            responseStructure.setData("OTP sent to the email of user");
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseStructure<User>> validotp(Long otp){
       ResponseStructure<User> responseStructure=new ResponseStructure<>();
       User user = userDao.findByOtp(otp);
       if(user != null){
           responseStructure.setStatus(HttpStatus.OK.value());
           responseStructure.setMessage("success");
           responseStructure.setData(user);
           return new ResponseEntity<>(responseStructure, HttpStatus.OK);
       }else {
           responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
           responseStructure.setMessage("Invalid OTP");
           responseStructure.setData(null);
           return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
       }
    }
   public ResponseEntity<ResponseStructure<User>> updatePassword(String password , Long otp){
        ResponseStructure<User> responseStructure=new ResponseStructure<>();
        User user = userDao.findByOtp(otp);
        if(user == null){
            responseStructure.setStatus(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("password cannot updated, invalid otp");
            responseStructure.setData(null);
            return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
        }else{
            user.setPassword(Aes.encrypt(password));
            user.setOtp(otp);
            userDao.saveUser(user);
            responseStructure.setStatus(HttpStatus.OK.value());
            responseStructure.setMessage("password updated successfully"+user.getPassword());
            responseStructure.setData(user);
            return new ResponseEntity<>(responseStructure, HttpStatus.OK);
        }
    }
  }