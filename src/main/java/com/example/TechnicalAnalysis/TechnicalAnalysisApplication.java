package com.example.TechnicalAnalysis;

import com.example.TechnicalAnalysis.Nodes.Person;
import com.example.TechnicalAnalysis.Repositories.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableNeo4jRepositories
@SpringBootApplication
public class TechnicalAnalysisApplication {
	public static String SonarQube = "";
	public static void main(String[] args) {
		SpringApplication.run(TechnicalAnalysisApplication.class, args);
	}
	public static boolean isWindows() {
		return System.getProperty("filename.separator").equals("\\");
	}

    @Bean
    CommandLineRunner demo(PersonRepository personRepository) {
        return args -> {
            personRepository.deleteAll();

            Person kostas = new Person("kostas", "thomson");
            Person aristea = new Person("aristea", "kotoula");
            Person kyriakos = new Person("kyriakos", "thomson");

            personRepository.save(kostas);
            personRepository.save(aristea);
            personRepository.save(kyriakos);

        };
    }
}
