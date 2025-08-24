package main.utils;

import main.config.AppConstants;
import main.repository.DrivingLicenseRepository;
import main.repository.VehicleRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;

public class GenerationUtils {
    private static final Random random = new Random();
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // generates license plate with first 2 letters being state code the next two being random digits.
    // the next 2 being random letters and the last 4 being random digits
    public static String generateLicensePlate(String stateName) {
        String stateCode = AppConstants.stateCodes.get(stateName);

        String districtCode = String.format("%02d", random.nextInt(100)); // 00-99

        String seriesCode = generateRandomAlphabets(2);

        String uniqueNumber = String.format("%04d", random.nextInt(10000)); // 0000-9999

        String license_plate = stateCode + districtCode + seriesCode + uniqueNumber;

        try {
            if (AppConstants.vehicleRepository.findByLicensePlate(license_plate)!=null) {
                return generateLicensePlate(stateName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return license_plate;
    }

    // generates random alphabets using the ALPHABET variable by using random to get an index
    private static String generateRandomAlphabets(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    // generates the license number with the first two letters being state code, the next two digits being random digits,
    // the next 4 being the year of issue of license and next 8 being random digits
    public static String generateLicenseNumber(String stateName) {
        String stateCode = AppConstants.stateCodes.get(stateName);

        String districtCode = String.format("%02d", random.nextInt(100)); // 00-99

        int currentYear = LocalDate.now().getYear();

        String uniqueNumber = String.format("%08d", random.nextInt(100000000)); // 00000000-99999999

        String license_number = stateCode + districtCode + currentYear + uniqueNumber;

        try {
            if (AppConstants.drivingLicenseRepository.findByNumber(license_number)!=null) {
                return generateLicenseNumber(stateName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return license_number;
    }

    // generates the engine number with 15 random alphabets and numbers using ALPHANUMERIC variable
    public static String generateEngineNumber() {
        String engine_number = generateRandomAlphanumeric(15);
        try {
            if (AppConstants.vehicleRepository.findByEngineNumber(engine_number)!=null) {
                return generateEngineNumber();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return engine_number;
    }

    // generates the chasis number with 17 random alphabets and numbers using ALPHANUMERIC variable
    public static String generateChassisNumber() {
        String chasis_number = generateRandomAlphanumeric(17);
        try {
            if (AppConstants.vehicleRepository.findByChasisNumber(chasis_number)!=null) {
                return generateChassisNumber();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return chasis_number;
    }

    // generates random alphabets and numbers with the length provided using ALPHANUMERIC variable
    private static String generateRandomAlphanumeric(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }
}
