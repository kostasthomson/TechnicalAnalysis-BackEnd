package com.Server.TechnicalAnalysis;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@SpringBootApplication
@EnableTransactionManagement
@EnableNeo4jRepositories
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TechnicalAnalysisApplication {
    public static void main(String[] args) {
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
        SpringApplication.run(TechnicalAnalysisApplication.class, args);
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }

}
