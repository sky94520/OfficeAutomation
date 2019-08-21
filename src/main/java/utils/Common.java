package utils;

public class Common {
    public static String getExtension(String filename){
        int index = filename.lastIndexOf('.');
        String suffix = filename.substring(index + 1);
        return suffix;
    }
}
