package com.Server.TechnicalAnalysis.Models;

public abstract class GitHubMetricEntity {
    protected Integer complexity;
    protected Integer loc;
    protected Integer td;
    protected Integer numFiles;
    protected Integer functions;
    protected Integer commentLines;
    protected Integer codeSmells;

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
}
