/**
 * Service de validation de diagrammes PlantUML.
 * Corrige la faille de faible couverture de tests identifiée dans le TP1.
 */
public class DiagramValidator {

    /**
     * Vérifie si un texte PlantUML est valide.
     * Un diagramme valide doit commencer par @startuml et finir par @enduml.
     */
    public boolean isValid(String diagram) {
        if (diagram == null || diagram.trim().isEmpty()) {
            return false;
        }
        String trimmed = diagram.trim();
        return trimmed.startsWith("@startuml") && trimmed.endsWith("@enduml");
    }

    /**
     * Compte le nombre de lignes dans un diagramme.
     */
    public int countLines(String diagram) {
        if (diagram == null || diagram.trim().isEmpty()) {
            return 0;
        }
        return diagram.split("\n").length;
    }

    /**
     * Retourne le type de diagramme détecté.
     */
    public String detectType(String diagram) {
        if (diagram == null || diagram.trim().isEmpty()) {
            return "UNKNOWN";
        }
        if (diagram.contains("->") || diagram.contains("-->")) {
            return "SEQUENCE";
        }
        if (diagram.contains("class ")) {
            return "CLASS";
        }
        if (diagram.contains("[") && diagram.contains("]")) {
            return "COMPONENT";
        }
        return "OTHER";
    }

    /**
     * Retourne un résumé du diagramme.
     */
    public String getSummary(String diagram) {
        if (!isValid(diagram)) {
            return "Diagramme invalide";
        }
        return "Type: " + detectType(diagram) 
            + " | Lignes: " + countLines(diagram);
    }
}
