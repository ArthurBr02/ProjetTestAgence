
# Instructions techniques

- _Créer un projet avec Gradle_
- _Ajout de la javadoc pour chacune de vos méthodes_
- _Tester le projet avec JUnit 5_
- _Réaliser les assertions avec la librairie ‘AssertJ’_
- _Réaliser des tests doubles avec le Framework ‘Mockito’_
- _Pouvoir lancer les tests en ligne de commande avec Gradle_
- _Pouvoir exécuter uniquement les tests du package `util`_
- _Pouvoir exécuter uniquement les tests du package `agency`_
- _Générer 2 rapports de couverture de code avec Jacoco : XML + HTML_

# Compte-rendu

Documentez votre travail à travers un guide :
- Détailler les outils (leur version…), de l'environnement utilisé.
```bash
java : 21
gradle : 8.6
AssertJ : 3.11.1
Mockito : 4
```

- Procédure pour créer un nouveau projet Java avec Gradle.
```bash
$ gradle init
-> choisir application, Groovy et laisser par défaut pour le reste
```

- Procédure pour importer les différentes librairies, plugins.
```bash
# build.gradle
dependencies {
    ...
    testImplementation "org.assertj:assertj-core:3.11.1"

    // Mockito
    testImplementation 'org.mockito:mockito-core:4.+'
    testImplementation 'org.mockito:mockito-junit-jupiter:4.+'
    ...
}

plugins {
    id 'jacoco'
}

test {
    finalizedBy jacocoTestReport // report is always generated after tests run
}
jacocoTestReport {
    dependsOn test // tests are required to run before generating the report
}

jacoco {
    toolVersion = "0.8.11"
    reportsDirectory = layout.buildDirectory.dir('reports/jacoco')
}

jacocoTestReport {
    reports {
        xml.required = true
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir('jacocoHtml')
    }
}

tasks.register('util', Test) {
    // Use JUnit Platform for integration tests.
    useJUnitPlatform {
        includeTags "util"
    }
    finalizedBy(jacocoTestReport)
}

tasks.register('agency', Test) {
    // Use JUnit Platform for integration tests.
    useJUnitPlatform {
        includeTags "agency"
    }
    finalizedBy(jacocoTestReport)
}
```
- Procédure pour lancer les différents tests avec Gradle et consulter les rapports.
```bash
$ gradle test # lancer tous les tests
$ gradle util # lancer les tests du package utils
$ gradle agency # lancer les tests du package agency
$ gradle jacocoTestReport # générer les rapports
```