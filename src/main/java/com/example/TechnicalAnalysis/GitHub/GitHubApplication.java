package com.example.TechnicalAnalysis.GitHub;

import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GitHubApplication {
    public static void main(String[] args) throws IOException, ParseException {
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://api.github.com/user/followers"))
//                .build();
//        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                .thenApply(HttpResponse::body)
//                .thenAccept(System.out::println)
//                .join();

        Process proc = Runtime.getRuntime().exec("cmd /c gh api /repos/kostasthomson/BlackJack/collaborators");
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        GitHubResponseController.ParseResponse(inputReader);
        GitHubResponseController.PrintResponse();
    }
}
