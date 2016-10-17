package com.myown.application;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
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
        Status call = git.status().call();
        System.out.println("modified size: "+call.getModified().size());
        System.out.println("uncommitedChanges size: "+call.getUncommittedChanges().size());
        System.out.println("untracked size: "+call.getUntracked().size());

        Set<String> changed = call.getModified();
        for (String s : changed) {
            System.out.println("modified: "+s);
        }

        Set<String> untracked = call.getUntracked();
        for (String s : untracked) {
            System.out.println("untracked: "+s);
        }

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
