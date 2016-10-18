package com.myown.application;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.util.Set;

public class GitUtils {
    public static void printStatus(Git git) {
        try {
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

            Set<String> untrackedFolders = call.getUntrackedFolders();
            for (String untrackedFolder : untrackedFolders) {
                System.out.println("untracked folders: " + untrackedFolder);
            }
        } catch (GitAPIException e) {
            throw new RuntimeException("error calling git status",e);
        }

    }
}
