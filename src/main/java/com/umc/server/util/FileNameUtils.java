package com.umc.server.util;

public class FileNameUtils {
    public static final String FILE_EXTENSION_SEPARATOR = ".";

    // TODO: 업로드시 파일 이름
    public static String newFileName(String fileName) {

        String now = String.valueOf(System.currentTimeMillis());

        int fileExtensionIndex = fileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        if (fileExtensionIndex == -1) {
            return fileName
                    + "_"
                    + System.currentTimeMillis(); // No extension found, just add timestamp
        }

        String fileExtension = fileName.substring(fileExtensionIndex);
        String originalFileName = fileName.substring(0, fileExtensionIndex);
        return originalFileName + "_" + now + fileExtension;
    }
}
