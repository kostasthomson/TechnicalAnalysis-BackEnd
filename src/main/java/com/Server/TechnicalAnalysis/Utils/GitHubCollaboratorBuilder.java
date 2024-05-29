package com.Server.TechnicalAnalysis.Utils;

import com.Server.TechnicalAnalysis.Models.GitHubCollaborator;
import com.Server.TechnicalAnalysis.Utils.Lists.GitHubCollaboratorList;

public class GitHubCollaboratorBuilder {
    private static final GitHubCollaboratorList collaboratorList = new GitHubCollaboratorList();

    public static GitHubCollaborator createCollaborator(String[] info) {
        if (collaboratorList.get(info[1]) != null) return null; // key = email
        GitHubCollaborator collaborator = new GitHubCollaborator(info[0], info[1]);
        collaboratorList.add(collaborator);
        return collaborator;
    }

    public static GitHubCollaborator getCollaborator(String key) {
        return collaboratorList.get(key);
    }

    public static GitHubCollaboratorList getList() {
        return collaboratorList;
    }
}
