package bg.sofia.uni.fmi.mjt.git;

public class Result {
    private String message;
    private boolean isSuccessful;

    public Result(String message, boolean isSuccessful) {
        this.message = message;
        this.isSuccessful = isSuccessful;
    }

    public boolean isSuccessful() {
        return  isSuccessful;
    }

    String getMessage() {
        return message;
    }
}
