package main.config;

import main.repository.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AppConstants {
    // Defines the ROLE CONSTANTS
    public static final String ROLE_USER = "User";
    public static final String ROLE_OFFICER = "Officer";

    // Defines the VEHICLE CONSTANTS
    public static final String VEHICLE_ACTIVE = "Active";
    public static final String VEHICLE_EXPIRED = "Expired";
    public static final String VEHICLE_SUSPENDED = "Suspended";

    // Defines the LICENSE CONSTANTS
    public static final String LICENSE_ACTIVE = "Active";
    public static final String LICENSE_EXPIRED = "Expired";

    // Defines the PAYMENT CONSTANTS
    public static final String PAYMENT_PAID = "Paid";
    public static final String PAYMENT_UNPAID = "Unpaid";

    // Defines the CHALLAN CONSTANTS
    public static final String CHALLAN_PENDING = "Pending";
    public static final String CHALLAN_PAID = "Paid";

    // Defines the RESULT CONSTANTS
    public static final String RESULT_PASS = "Pass";
    public static final String RESULT_FAIL = "Fail";
    public static final String RESULT_PENDING = "Pending";

    public static final DrivingExamRepository drivingExamRepository = new DrivingExamRepository();
    public static final VehicleRepository vehicleRepository = new VehicleRepository();
    public static final DrivingLicenseRepository drivingLicenseRepository = new DrivingLicenseRepository();
    public static final EChallanRepository eChallanRepository = new EChallanRepository();
    public static final PaymentRepository paymentRepository = new PaymentRepository();
    public static final UserRepository userRepository = new UserRepository();

    // Defines the PAYMENT MODE CONSTANTS
    public static final String[] PAYMENT_MODE={"Credit Card",
            "Debit Card",
            "Google Pay (GPay)",
            "PhonePe",
            "Paytm",
            "Net Banking",
            "Direct Bank Transfer"};

    // Defines the PAYMENT REASON CONSTANTS
    public static final String[] PAYMENT_REASON={"Challan",
            "Driving Exam", "PUC", "Insurance", "RC", "License Renewal"};

    // Defines the DATE FORMAT CONSTANT
    public static final String DATE_FORMAT = "dd-MM-yyyy";

    // Defines the STATE NAMES LIST AND HASHMAP
    public static final List<String> STATES_NAMES = Arrays.asList(
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chhattisgarh",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttar Pradesh",
            "Uttarakhand",
            "West Bengal");

    public static final HashMap<String, String> stateCodes = new HashMap<>();
    static {
        stateCodes.put("Andhra Pradesh", "AP");
        stateCodes.put("Arunachal Pradesh", "AR");
        stateCodes.put("Assam", "AS");
        stateCodes.put("Bihar", "BR");
        stateCodes.put("Chhattisgarh", "CG");
        stateCodes.put("Goa", "GA");
        stateCodes.put("Gujarat", "GJ");
        stateCodes.put("Haryana", "HR");
        stateCodes.put("Himachal Pradesh", "HP");
        stateCodes.put("Jharkhand", "JH");
        stateCodes.put("Karnataka", "KA");
        stateCodes.put("Kerala", "KL");
        stateCodes.put("Madhya Pradesh", "MP");
        stateCodes.put("Maharashtra", "MH");
        stateCodes.put("Manipur", "MN");
        stateCodes.put("Meghalaya", "ML");
        stateCodes.put("Mizoram", "MZ");
        stateCodes.put("Nagaland", "NL");
        stateCodes.put("Odisha", "OD");
        stateCodes.put("Punjab", "PB");
        stateCodes.put("Rajasthan", "RJ");
        stateCodes.put("Sikkim", "SK");
        stateCodes.put("Tamil Nadu", "TN");
        stateCodes.put("Telangana", "TS");
        stateCodes.put("Tripura", "TR");
        stateCodes.put("Uttar Pradesh", "UP");
        stateCodes.put("Uttarakhand", "UK");
        stateCodes.put("West Bengal", "WB");
    }


    // Defines the CATEGORIES CONSTANT
    public static final HashMap<Integer, String> CATEGORIES;

    static {
        try {
            CATEGORIES = vehicleRepository.getAllCategory();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
