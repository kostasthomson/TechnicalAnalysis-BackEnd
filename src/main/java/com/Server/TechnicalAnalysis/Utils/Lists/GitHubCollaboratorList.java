package com.Server.TechnicalAnalysis.Utils.Lists;

import com.Server.TechnicalAnalysis.Models.GitHubCollaborator;

public class GitHubCollaboratorList extends GitHubEntityCollection<GitHubCollaborator> {
    @Override
    public boolean add(GitHubCollaborator object) {
        list.add(object);
        map.put(object.getEmail(), list.size()-1);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(((GitHubCollaborator) o).getEmail(), list.indexOf(o)) && list.remove(o);
    }
}
