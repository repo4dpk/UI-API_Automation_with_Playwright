package com.ecommerce.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads test data from JSON files in the testdata directory.
 */
public class TestDataProvider {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, JsonNode> cache = new HashMap<>();

    private TestDataProvider() {}

    /**
     * Load test data from a JSON file.
     *
     * @param fileName Name of the JSON file (without path)
     * @return JsonNode containing the parsed data
     */
    public static JsonNode loadTestData(String fileName) {
        return cache.computeIfAbsent(fileName, key -> {
            String path = "testdata/" + key;
            try (InputStream input = TestDataProvider.class
                    .getClassLoader()
                    .getResourceAsStream(path)) {
                if (input == null) {
                    throw new RuntimeException(
                        "Test data file not found: " + path
                    );
                }
                LoggerUtil.info("Test data loaded from: " + path);
                return objectMapper.readTree(input);
            } catch (IOException e) {
                throw new RuntimeException(
                    "Failed to load test data: " + path, e
                );
            }
        });
    }

    /**
     * Get user credentials by user type.
     */
    public static Map<String, String> getUserCredentials(String userType) {
        JsonNode users = loadTestData("users.json");
        JsonNode user = users.get(userType);

        if (user == null) {
            throw new RuntimeException(
                "User type not found: " + userType
            );
        }

        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", user.get("email").asText());
        credentials.put("password", user.get("password").asText());

        if (user.has("name")) {
            credentials.put("name", user.get("name").asText());
        }

        LoggerUtil.info("Loaded credentials for user type: " + userType);
        return credentials;
    }

    /**
     * Clear cached test data.
     */
    public static void clearCache() {
        cache.clear();
    }
}
