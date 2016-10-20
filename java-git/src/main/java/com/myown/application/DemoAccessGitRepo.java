package com.myown.application;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import org.apache.commons.lang.RandomStringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileBasedConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;

import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

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

        System.out.println("git dir: " + repository.getDirectory().toPath().toString());
        FileBasedConfig c = (FileBasedConfig)repository.getConfig();
        String url = c.getString("remote", "origin", "url");
        System.out.println("remote origin "+url);

        StoredConfig config = repository.getConfig();
        RemoteConfig remoteConfig = new RemoteConfig(config, "origin");
        URIish uri = new URIish("ssh://git@github.com:frtj/frtj-java-examples.git");
        remoteConfig.addURI(uri);
        remoteConfig.update(config);
        config.save();


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
                FileUtils.writeToFile(testFile, RandomStringUtils.randomAlphabetic(10));
                RevCommit commit = git.commit().setAll(true).setMessage( "added text to test.txt" ).call();
            }
        }

        String s = FileUtils.readFile(currentPath.resolve("../mygitconfig.json"));
        System.out.println(s);
        JsonObject parse = JavaxJsonUtil.parse(s);
        System.out.println(parse.getString("test"));
        System.out.println(parse.getString("keypass"));


        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host host, Session session ) {

            }
        };


        PushCommand push = git.push();

        push.setTransportConfigCallback(
                new TransportConfigCallback() {
                    @Override
                    public void configure( Transport transport ) {
                        SshTransport sshTransport = ( SshTransport )transport;
                        sshTransport.setSshSessionFactory( sshSessionFactory );
                    }
                }
        );
        Iterable<PushResult> iterable = push.call();

        PushResult pushResult = iterable.iterator().next();
        RemoteRefUpdate.Status status = pushResult.getRemoteUpdate( "refs/heads/master" ).getStatus();
        System.out.println(status.toString());
       /* Iterable<PushResult> iterable = git.push().call();
        PushResult pushResult = iterable.iterator().next();
        RemoteRefUpdate.Status status = pushResult.getRemoteUpdate( "refs/heads/master" ).getStatus();
        System.out.println(status.toString());*/


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
