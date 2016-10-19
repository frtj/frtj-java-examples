package com.myown.application;

import java.io.BufferedReader;
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

    public static String readFile(Path path) {
        StringBuilder b = new StringBuilder();
        if (Files.exists(path) && Files.isRegularFile(path)) {
            Charset charset = Charset.forName("UTF-8");
            try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    b.append(line);
                    if (b.length() > 0) {
                        b.append("\n");
                    }
                }
            } catch (IOException x) {
                throw new RuntimeException("Problem reading file: " + path, x);
            }
        } else {
            throw new RuntimeException("no file found: " + path);
        }
        return b.toString();
    }
}
