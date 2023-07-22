package com.example.TechnicalAnalysis.GitHub;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;

public abstract class GitHubResponseController {

    public static void ParseResponse(Reader in) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object o = parser.parse(in);
        if (o instanceof JSONArray) {
            GitHubCollaboratorList.addAll((JSONArray) o);
        }
    }

    public static void PrintResponse() {
        GitHubCollaboratorList.PrintList();
    }
}
