package com.myown.application;

import javax.json.JsonObject;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        System.out.println("Main");

        Path currentPath = Paths.get("").toAbsolutePath();
        System.out.println("current path: " + currentPath.toString());

        Path subProjectFolder = currentPath.resolve("../").normalize();
        System.out.println(subProjectFolder.toAbsolutePath().toString());

        //String s = FileUtils.readFile(Paths.get("java-git/src/main/resources/test.txt"));
        String s = FileUtils.readFile(subProjectFolder.resolve("mygitconfig.json"));
        System.out.println(s);
        JsonObject parse = JavaxJsonUtil.parse(s);
        System.out.println(parse.getString("hmm"));
    }
}
