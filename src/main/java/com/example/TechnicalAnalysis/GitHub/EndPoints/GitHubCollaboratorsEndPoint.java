package com.example.TechnicalAnalysis.GitHub.EndPoints;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;
import com.example.TechnicalAnalysis.GitHub.Entities.Utils.GitHubCollaboratorList;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class GitHubCollaboratorsEndPoint extends GitHubEndPoint{
    private final GitHubCollaboratorList list = new GitHubCollaboratorList();
    public GitHubCollaboratorsEndPoint() {
        super("collaborators");
    }

    @Override
    public void ParseResponse(Reader in) throws IOException, ParseException {
        Object o = parser.parse(in);
        if (o instanceof JSONArray) {
            list.addAll((JSONArray) o);
        }
    }

    @Override
    public void ParseResponse(String in) {
        try {
            Object o = parser.parse(in);
            if (o instanceof JSONArray) {
                list.addAll((JSONArray) o);
            }
        } catch (ParseException pe) {
            System.out.println("Exception: ParseException");
        }
    }

    @Override
    public void PrintResponse() {
        list.printList();
    }

    @Override
    public void PrintResponse(Void unused) {
        this.PrintResponse();
    }

    @Override
    public List<GitHubEntity> getList() {
        return this.list.getList();
    }
}
