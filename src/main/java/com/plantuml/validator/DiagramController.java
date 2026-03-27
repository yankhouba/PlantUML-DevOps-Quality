package com.plantuml.validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST pour la validation de diagrammes PlantUML.
 * Expose les endpoints de l'API.
 */
@RestController
@RequestMapping("/api")
public class DiagramController {

    private final DiagramValidator validator = new DiagramValidator();

    /**
     * Endpoint de validation d'un diagramme PlantUML.
     * POST /api/validate
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(
            @RequestBody String diagram) {

        Map<String, Object> response = new HashMap<>();
        response.put("valide", validator.isValid(diagram));
        response.put("type", validator.detectType(diagram));
        response.put("lignes", validator.countLines(diagram));
        response.put("resume", validator.getSummary(diagram));

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de santé du service.
     * GET /api/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "PlantUML Validator API");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de statistiques du service.
     * GET /api/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "PlantUML Validator API");
        response.put("version", "1.0");
        response.put("endpoints", new String[]{
            "POST /api/validate",
            "GET /api/health",
            "GET /api/stats"
        });
        return ResponseEntity.ok(response);
    }
}