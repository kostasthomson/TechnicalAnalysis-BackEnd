package com.Server.TechnicalAnalysis.Services;

import com.Server.TechnicalAnalysis.Models.GitHubFile;
import com.Server.TechnicalAnalysis.Repositories.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private FileRepository repository;

    public List<GitHubFile> getAllFiles() {
        return this.repository.findAll();
    }

    public List<GitHubFile> getFile(String name) {
        return this.repository.findByName(name);
    }

}
