package com.Server.TechnicalAnalysis.Models;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Random;

@Node("File")
public class GitHubFile implements GitHubEntity {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String node_id;
    private String name;
    private String path;
    private Integer complexity;
    private Integer loc;
    private Integer td;
    private Integer numFiles;
    private Integer functions;
    private Integer commentLines;
    private Integer codeSmells;

    public GitHubFile() {
    }

    public GitHubFile(String fileName) {
        this.path = fileName;
        String[] nameArray = fileName.split("/");
        this.name = nameArray[nameArray.length - 1];
    }

    public GitHubFile(String name, Integer complexity, Integer loc, Integer td) {
        this.name = name;
        this.complexity = complexity;
        this.loc = loc;
        this.td = td;
    }

    public boolean isJava() {
        return this.name.endsWith(".java");
    }

    public Integer getNumFiles() {
        return numFiles;
    }

    public void setNumFiles(Integer num_files) {
        this.numFiles = num_files;
    }

    public Integer getFunctions() {
        return functions;
    }

    public void setFunctions(Integer functions) {
        this.functions = functions;
    }

    public Integer getCommentLines() {
        return commentLines;
    }

    public void setCommentLines(Integer comment_lines) {
        this.commentLines = comment_lines;
    }

    public Integer getCodeSmells() {
        return codeSmells;
    }

    public void setCodeSmells(Integer code_smells) {
        this.codeSmells = code_smells;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public Integer getComplexity() {
        return complexity;
    }

    public void setComplexity(Integer complexity) {
        this.complexity = complexity;
    }

    public Integer getLoc() {
        return loc;
    }

    public void setLoc(Integer loc) {
        this.loc = loc;
    }

    public Integer getTd() {
        return td;
    }

    public void setTd(Integer td) {
        this.td = td;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
