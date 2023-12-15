package utils;

public class Utils {
    private Utils() {
    }


    public static <T> void log(T value) {
        if (value == null) throw new RuntimeException("Null value provided");

        System.out.println(value);
    }
}
