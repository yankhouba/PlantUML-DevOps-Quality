import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour DiagramValidator.
 * Objectif : atteindre 80%+ de couverture de tests.
 * Corrige la faille #3 identifiée dans le TP1 (couverture ~7%).
 */
public class DiagramValidatorTest {

    private DiagramValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DiagramValidator();
    }

    // ===== TESTS isValid() =====

    @Test
    void testDiagramValideSimple() {
        String diagram = "@startuml\nAlice -> Bob: Hello\n@enduml";
        assertTrue(validator.isValid(diagram));
    }

    @Test
    void testDiagramSansStartuml() {
        String diagram = "Alice -> Bob: Hello\n@enduml";
        assertFalse(validator.isValid(diagram));
    }

    @Test
    void testDiagramSansEnduml() {
        String diagram = "@startuml\nAlice -> Bob: Hello";
        assertFalse(validator.isValid(diagram));
    }

    @Test
    void testDiagramNull() {
        assertFalse(validator.isValid(null));
    }

    @Test
    void testDiagramVide() {
        assertFalse(validator.isValid(""));
    }

    @Test
    void testDiagramEspaceSeulement() {
        assertFalse(validator.isValid("   "));
    }

    @Test
    void testDiagramAvecEspacesAutour() {
        // trim() doit enlever les espaces avant de vérifier
        String diagram = "  @startuml\nAlice -> Bob\n@enduml  ";
        assertTrue(validator.isValid(diagram));
    }

    // ===== TESTS countLines() =====

    @Test
    void testCountLines3Lignes() {
        String diagram = "@startuml\nAlice -> Bob\n@enduml";
        assertEquals(3, validator.countLines(diagram));
    }

    @Test
    void testCountLines4Lignes() {
        String diagram = "@startuml\nAlice -> Bob\nBob -> Alice\n@enduml";
        assertEquals(4, validator.countLines(diagram));
    }

    @Test
    void testCountLinesNull() {
        assertEquals(0, validator.countLines(null));
    }

    @Test
    void testCountLinesVide() {
        assertEquals(0, validator.countLines(""));
    }

    @Test
    void testCountLinesDiagrammeLong() {
        // Diagramme avec 102 lignes
        // Corrige la faille : tester les cas extrêmes
        StringBuilder sb = new StringBuilder("@startuml\n");
        for (int i = 0; i < 100; i++) {
            sb.append("Alice -> Bob: Message ").append(i).append("\n");
        }
        sb.append("@enduml");
        assertEquals(102, validator.countLines(sb.toString()));
    }

    // ===== TESTS detectType() =====

    @Test
    void testDetectTypeSequence() {
        String diagram = "@startuml\nAlice -> Bob: Hello\n@enduml";
        assertEquals("SEQUENCE", validator.detectType(diagram));
    }

    @Test
    void testDetectTypeSequenceAvecFleche() {
        // --> est aussi une flèche de séquence
        String diagram = "@startuml\nAlice --> Bob: Hello\n@enduml";
        assertEquals("SEQUENCE", validator.detectType(diagram));
    }

    @Test
    void testDetectTypeClass() {
        String diagram = "@startuml\nclass Animal\n@enduml";
        assertEquals("CLASS", validator.detectType(diagram));
    }

    @Test
    void testDetectTypeComponent() {
        String diagram = "@startuml\n[Component1]\n@enduml";
        assertEquals("COMPONENT", validator.detectType(diagram));
    }

    @Test
    void testDetectTypeNull() {
        assertEquals("UNKNOWN", validator.detectType(null));
    }

    @Test
    void testDetectTypeVide() {
        assertEquals("UNKNOWN", validator.detectType(""));
    }

    @Test
    void testDetectTypeOther() {
        // Diagramme sans flèche, class ou composant
        String diagram = "@startuml\nnote: bonjour\n@enduml";
        assertEquals("OTHER", validator.detectType(diagram));
    }

    // ===== TESTS getSummary() =====

    @Test
    void testGetSummarySequence() {
        String diagram = "@startuml\nAlice -> Bob\n@enduml";
        String summary = validator.getSummary(diagram);
        assertTrue(summary.contains("SEQUENCE"));
        assertTrue(summary.contains("3"));
    }

    @Test
    void testGetSummaryClass() {
        String diagram = "@startuml\nclass Animal\n@enduml";
        String summary = validator.getSummary(diagram);
        assertTrue(summary.contains("CLASS"));
        assertTrue(summary.contains("3"));
    }

    @Test
    void testGetSummaryComponent() {
        String diagram = "@startuml\n[MonComposant]\n@enduml";
        String summary = validator.getSummary(diagram);
        assertTrue(summary.contains("COMPONENT"));
        assertTrue(summary.contains("3"));
    }

    @Test
    void testGetSummaryInvalide() {
        assertEquals("Diagramme invalide", validator.getSummary("pas valide"));
    }

    @Test
    void testGetSummaryNull() {
        assertEquals("Diagramme invalide", validator.getSummary(null));
    }
}