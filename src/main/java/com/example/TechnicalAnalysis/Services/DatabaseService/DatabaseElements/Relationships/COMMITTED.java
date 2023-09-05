package com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Relationships;

import com.example.TechnicalAnalysis.Services.DatabaseService.DatabaseElements.Nodes.GitHubCollaborator;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class COMMITTED {
    @RelationshipId
    private long relationshipId;

    @TargetNode
    private GitHubCollaborator collaborator;
}
