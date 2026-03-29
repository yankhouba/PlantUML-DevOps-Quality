# Étape 1 - Construction
# On utilise une image Java 17 pour compiler
FROM maven:3.9-eclipse-temurin-17 AS build

# On définit le dossier de travail
WORKDIR /app

# On copie le pom.xml pour télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# On copie tout le code source
COPY src ./src
COPY google_checks.xml .

# On compile et on crée le JAR
RUN mvn clean package -DskipTests

# Étape 2 - Exécution
# Image plus légère juste pour lancer l'application
FROM eclipse-temurin:17-jre

# Dossier de travail
WORKDIR /app

# On copie seulement le JAR compilé
COPY --from=build /app/target/plantuml-quality-1.0-SNAPSHOT.jar app.jar

# On expose le port 8080
EXPOSE 8080

# On lance l'application
ENTRYPOINT ["java", "-jar", "app.jar"]