package com.myown.application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    public static void writeToFile(Path path, String content) {
        Charset charset = Charset.forName("UTF-8");
        writeToFile(path, content, charset);
    }

    public static void writeToFile(Path path, String content, Charset charset) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
            writer.write(content, 0, content.length());
        } catch (IOException x) {
            throw new RuntimeException("Error saving file: " + path.toString());
        }
    }
}
