package com.example;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Random;

public class JsonHashgenerator {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar <jar_file> <PRN_Number> <Path_To_JSON_File>");
            return;
        }

        String prnNumber = args[0].toLowerCase().replaceAll("\\s+", ""); // Ensure lowercase and remove spaces
        String jsonFilePath = args[1];

        try {
         
            JSONObject jsonObject = readJsonFile(jsonFilePath);

          
            String destinationValue = getFirstDestinationValue(jsonObject);

            if (destinationValue == null) {
                System.out.println("No 'destination' key found in the JSON file.");
                return;
            }

        
            String randomString = generateRandomString(8);

       
            String md5Hash = generateMD5Hash(prnNumber + destinationValue + randomString);

         
            System.out.println(md5Hash + ";" + randomString);

        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error generating MD5 hash: " + e.getMessage());
        }
    }

  
    private static JSONObject readJsonFile(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return new JSONObject(new JSONTokener(reader));
        }
    }

    // Method to traverse JSON and get the first "destination" value
    private static String getFirstDestinationValue(JSONObject jsonObject) {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
            if (key.equals("destination")) {
                return value.toString();
            } else if (value instanceof JSONObject) {
                String result = getFirstDestinationValue((JSONObject) value);
                if (result != null) return result;
            }
        }
        return null; // Return null if not found
    }

    // Method to generate a random alphanumeric string of specified length
    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            randomString.append(chars.charAt(random.nextInt(chars.length())));
        }
        return randomString.toString();
    }

    // Method to generate MD5 hash
    private static String generateMD5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] digest = md.digest();

        // Convert byte array to hex format
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
