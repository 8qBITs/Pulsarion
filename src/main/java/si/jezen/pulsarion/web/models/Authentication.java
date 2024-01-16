package si.jezen.pulsarion.web.models;

import lombok.Getter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Authentication {
    @Getter
    private String username, password;

    public Authentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getAuthToken() {
        return sha256(username, password);
    }

    public static String sha256(String param1, String param2) {
        String hashable = param1+param2;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(hashable.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : bytes) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
