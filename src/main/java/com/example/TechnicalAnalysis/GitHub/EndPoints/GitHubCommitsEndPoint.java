package com.example.TechnicalAnalysis.GitHub.EndPoints;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubCommit;
import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;
import com.example.TechnicalAnalysis.GitHub.Entities.Utils.GitHubCommitList;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class GitHubCommitsEndPoint extends GitHubEndPoint {
    private final GitHubCommitList list = new GitHubCommitList();
    public GitHubCommitsEndPoint() {
        super("commits");
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
        }catch (ParseException pe) {
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

    public void UpdateCommit(GitHubCommit e, GitHubCommit ghc) {
        this.list.UpdateCommit(e, ghc);
    }

    public List<GitHubEntity> getList() {
        return this.list.getList();
    }
}
