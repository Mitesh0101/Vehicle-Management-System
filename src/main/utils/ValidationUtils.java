package main.utils;
import main.config.AppConstants;

import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import java.time.format.ResolverStyle;
import java.util.*;
public class ValidationUtils {
    // checks if the email contains @ and ends with .com
    public static boolean isValidEmail(String email)
    {
        if (email == null) return false;

        // Must contain '@'
        int atIndex = email.indexOf('@');
        if (atIndex <= 0) return false; // no '@' or nothing before it

        // Must contain '.' after '@'
        int dotIndex = email.lastIndexOf('.');
        if (dotIndex < atIndex + 2) return false; // dot must be after at least 1 char from '@'

        // Must not end with '.'
        if (dotIndex == email.length() - 1) return false;

        return true;
    }

    // checks if the phone number is 10 digits long and only contains digits
    public static boolean isValidPhone(String phone)
    {
        if(phone==null || phone.length()!=10)
        {
            return false;
        }
        else if (!phone.startsWith("9") && !phone.startsWith("8") && !phone.startsWith("7")) {
            return false;
        }

        for (int i=0;i<phone.length();i++)
        {
            char c=phone.charAt(i);
            if(!Character.isDigit(c))
            {
                return false;
            }
        }
        return true;
    }

    // checks if the username's length is greater than 3 and less than 20 and contains only letters and digits
    public static boolean isValidUsername(String username)
    {
        if(username==null || username.length()<3 || username.length()>20)
        {
            return false;
        }

        for (int i=0;i<username.length();i++)
        {
            char c=username.charAt(i);
            if(!Character.isLetterOrDigit(c))
            {
                return false;
            }
        }
        return true;
    }

    // checks if the password contains both characters and digits
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char ch : password.toCharArray()) {
            if (Character.isLetter(ch)) {
                hasLetter = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            }

            if (hasLetter && hasDigit) {
                return true;
            }
        }
        return false;
    }

    // checks if the name contains only letters
    public static boolean isValidName(String name) {
        if (name == null) {
            return false;
        }

        name = name.trim();

        if (name.length() < 3) {
            return false;
        }

        for (char ch : name.toCharArray()) {
            if (!Character.isLetter(ch) && ch != ' ') {
                return false;
            }
        }

        return true;
    }


    // checks if the Date of Birth is of the format dd-MM-yyyy
    public static boolean isValidDOB(String dobString) {
        if (dobString == null || dobString.trim().isEmpty()) {
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);
            LocalDate dob = LocalDate.parse(dobString, formatter);
            LocalDate today = LocalDate.now();
            LocalDate earliestValid = LocalDate.of(1900, 1, 1);

            return !dob.isAfter(today) && !dob.isBefore(earliestValid);

        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // checks if the driving license number has first 2 letters and last 14 digits
    public static boolean validateDrivingLicenseNumber(String licenseNumber) {
        if (licenseNumber == null || licenseNumber.length() != 16) {
            return false;
        }

        String license = licenseNumber.toUpperCase();

        if (!Character.isLetter(license.charAt(0)) || !Character.isLetter(license.charAt(1))) {
            return false;
        }

        for (int i = 2; i < 16; i++) {
            if (!Character.isDigit(license.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}