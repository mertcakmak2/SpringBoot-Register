package com.example.registration.registration;


import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.*;

@Service
public class EmailValidator {

    public static boolean validEmail(String email){
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);
        System.out.println(email +" : "+ matcher.matches());
        return matcher.matches();
    }

}
