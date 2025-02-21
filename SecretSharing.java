import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SecretSharing {

    public static void main(String[] args) throws IOException {
        String jsonFilePath = "C:\Users\Student\Desktop\Problem solving\test_cases.json"; // Path to your JSON file
        // Read the JSON file
        JsonNode rootNode = readJsonFile(jsonFilePath);

        // Parse the keys for n and k
        int n = rootNode.path("keys").path("n").asInt();
        int k = rootNode.path("keys").path("k").asInt();

        // List to hold decoded roots
        List<int[]> roots = new ArrayList<>();

        // Read the roots
        for (int i = 1; i <= n; i++) {
            JsonNode node = rootNode.path(String.valueOf(i));
            int base = node.path("base").asInt();
            String value = node.path("value").asText();
            int y = decodeBase(value, base);
            roots.add(new int[]{i, y});
        }

        // Now use Lagrange Interpolation to find the constant term 'c'
        int secret = findSecretUsingLagrangeInterpolation(roots, k);
        
        // Output the secret (constant term c)
        System.out.println("The secret (constant term 'c') is: " + secret);
    }

    // Method to read and parse the JSON file
    public static JsonNode readJsonFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(new File(filePath));
    }

    // Method to decode a value in a given base
    public static int decodeBase(String value, int base) {
        return Integer.parseInt(value, base);
    }

    // Method to perform Lagrange Interpolation and find the secret (constant term 'c')
    public static int findSecretUsingLagrangeInterpolation(List<int[]> roots, int k) {
        int secret = 0;

        // Use only the first 'k' roots
        for (int i = 0; i < k; i++) {
            int[] currentRoot = roots.get(i);
            int xi = currentRoot[0];
            int yi = currentRoot[1];
            
            int numerator = 1;
            int denominator = 1;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    int xj = roots.get(j)[0];
                    numerator *= -xj;
                    denominator *= (xi - xj);
                }
            }

            // Calculate the Lagrange basis polynomial for this term
            secret += yi * numerator / denominator;
        }

        return secret;
    }
}
