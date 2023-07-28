package com.example.TechnicalAnalysis.GitHub.EndPoints;

import com.example.TechnicalAnalysis.GitHub.Entities.GitHubEntity;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public abstract class GitHubEndPoint {
    private final String name;
    protected final JSONParser parser = new JSONParser();
    protected GitHubEndPoint(String ep_name) {
        this.name = ep_name;
    }

    public abstract void ParseResponse(Reader in) throws IOException, ParseException;
    public abstract void ParseResponse(String in);
    public abstract void PrintResponse();
    public abstract void PrintResponse(Void unused);
    @Override
    public String toString() {
        return this.name;
    }

    public abstract List<GitHubEntity> getList();

}
