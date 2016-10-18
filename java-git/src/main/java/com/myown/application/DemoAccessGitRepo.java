package com.myown.application;

import org.apache.commons.lang.RandomStringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * Created by frode on 17.10.2016.
 */
public class DemoAccessGitRepo {

    public static void main(String[] args) throws Exception {
        DemoAccessGitRepo demo = new DemoAccessGitRepo();
        demo.findGitRepo();
        demo.run();
    }

    private void run() throws Exception {

        Path currentPath = Paths.get("").toAbsolutePath();
        System.out.println("current path: " + currentPath.toString());

        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.readEnvironment(); //read git env config
        repositoryBuilder.setMustExist( true );
        repositoryBuilder.setGitDir(currentPath.resolve(".git").toFile());
        Repository repository = repositoryBuilder.build();

        if( repository.getObjectDatabase().exists() ) {
            System.out.println("unreliable: git repo exists");
        }

        Ref head = repository.findRef("HEAD");
        if( head != null ) {
            System.out.println("more reliable: git repo exists");
            System.out.println(head.getName());

        }

        Git git = new Git(repository);


        GitUtils.printStatus(git);

        Path localResourcesPath = Paths.get("java-git/src/main/resources");
        Path testFilePath = Paths.get("test.txt");

        Path resources = currentPath.resolve(localResourcesPath);
        if(!Files.exists(resources, LinkOption.NOFOLLOW_LINKS)) {
            Files.createDirectories(resources);
        }

        if(Files.exists(resources, LinkOption.NOFOLLOW_LINKS)) {
            Path testFile = resources.resolve(testFilePath);
            if(!Files.isRegularFile(testFile, LinkOption.NOFOLLOW_LINKS)) {
                System.out.println("created the file: " + testFile.toString());
                Files.createFile(testFile);
                DirCache index = git.add().addFilepattern( localResourcesPath.resolve(testFilePath).toString() ).call();
                RevCommit commit = git.commit().setAll(true).setMessage( "added test.txt" ).call();
                System.out.println("git commit'd: " + commit.toString());

            }
            if(Files.isRegularFile(testFile, LinkOption.NOFOLLOW_LINKS)) {
                System.out.println("testfile exists");
                FileUtils.writeToFile(testFile, RandomStringUtils.random(10));
                RevCommit commit = git.commit().setAll(true).setMessage( "added text to test.txt" ).call();
            }
        }

        GitUtils.printStatus(git);
    }

    private void findGitRepo() throws Exception {
        Path currentPath = Paths.get("").toAbsolutePath();
        System.out.println("current path: " + currentPath.toString());

        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.readEnvironment(); //read git env config
        repositoryBuilder.setMustExist( true );

        repositoryBuilder.addCeilingDirectory( currentPath.toFile() );
        repositoryBuilder.findGitDir( currentPath.toFile() );

        File gitDir = repositoryBuilder.getGitDir();
        Path gitDirPath = gitDir.toPath();

        System.out.println("found git repo: "+gitDirPath.toString());

        Repository repository = repositoryBuilder.build();

        if( repository.getObjectDatabase().exists() ) {
            System.out.println("unreliable: git repo exists");
        }

        if( repository.findRef( "HEAD" ) != null ) {
            System.out.println("more reliable: git repo exists");
        }
    }

}
