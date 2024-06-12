package com.Server.TechnicalAnalysis.Utils.Lists;

import com.Server.TechnicalAnalysis.Models.GitHubCollaborator;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class GitHubCollaboratorList extends GitHubEntityCollection<GitHubCollaborator> {
    public static GitHubCollaboratorList convert(List<GitHubCollaborator> collaborators) {
        GitHubCollaboratorList list = new GitHubCollaboratorList();
        collaborators.forEach(list::add);
        return list;
    }

    public void addAll(List<GitHubCollaborator> authors) {
        list.addAll(authors);
        authors.forEach(author -> map.put(author.getEmail(), list.indexOf(author)));
    }

    public void addAll(JsonNode array) {
        for (JsonNode obj : array) {
//            map.put(obj.get("id").asText(), new GitHubCollaborator(obj));
            map.put(obj.get("id").asText(), 0);
        }
    }

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
