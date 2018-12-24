package bg.sofia.uni.fmi.mjt.git;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Commit {
    private String hashCode;
    private String message;
    private String createdOn;

    public Commit(String message) {
        LocalDateTime date = LocalDateTime.now();

        DateTimeFormatter ft = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm yyyy");
        createdOn = ft.format(date);

        try {
            hashCode = hexDigest(createdOn + message);
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }

        this.message = message;
    }

    public String getHash() {
        return hashCode;
    }

    public String getMessage() {
        return message;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String hexDigest(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return convertBytesToHex(bytes);
    }

    private String convertBytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte current : bytes) {
            hex.append(String.format("%02x", current));
        }

        return hex.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commit commit = (Commit) o;
        return Objects.equals(hashCode, commit.hashCode) &&
                Objects.equals(message, commit.message) &&
                Objects.equals(createdOn, commit.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashCode, message, createdOn);
    }
}