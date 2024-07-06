# TechnicalAnalysis-BackEnd

Welcome to the TechnicalAnalysis-BackEnd repository! 
This project is part of my Bachelor's Thesis research, 
and it is developed to provide a comprehensive back-end application
for technical analysis of software projects. The application leverages 
code quality analysis tools and libraries to help users analyze and view 
software's technical debt.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Technologies](#technologies)


## Features

- RESTful API endpoints for interacting with the analysis data
- Integration with Dockerized SonarQube for code quality analysis
- Data storage in Dockerized Neo4j and PostgreSQL databases
- Secure and optimized data handling

## Installation

To set up the project locally, follow these steps:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/kostasthomson/TechnicalAnalysis-BackEnd.git
2. **Navigate to the project directory:**
   ```bash
   cd TechnicalAnalysis-BackEnd

3. **Build and run the Docker containers:**
    ```bash
    docker compose up --build

## Usage
After starting the application, the following services will be available:

- Spring Boot application at http://localhost:8085
- SonarQube server at http://localhost:9952
- Neo4j database at http://localhost:7474
- PostgreSQL database (used by SonarQube)

## API Documentation
The backend provides several API endpoints for accessing and processing analysis data. Below are some of the main endpoints:

- GET /api/init
  - Triggers initialization of a repository
- GET /api/export
  - Triggers exporting of analysis results
- GET /api/commits
  - Retrieves information about commits
- GET /api/projects
  - Retrieve information about a project

## Technologies
- Java & Spring Boot: For building the backend application
- Docker: For containerization of services
- SonarQube: For code quality analysis
- Neo4j: Graph database for storing analysis data
- ostgreSQL: Relational database for SonarQube data storage
- Maven: For dependency management and building the application