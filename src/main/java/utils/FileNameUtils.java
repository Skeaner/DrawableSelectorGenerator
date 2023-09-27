package utils;

public class FileNameUtils {
    public static String removeFileEndings(String fileName) {
        return fileName.replace(".9.png", "").replace(".png", "").replace(".jpg", "");
    }
}
