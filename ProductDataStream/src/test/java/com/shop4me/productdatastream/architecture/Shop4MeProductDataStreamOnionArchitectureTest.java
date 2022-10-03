package com.shop4me.productdatastream.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AnalyzeClasses(packages = {
        "com.shop4me.productdatastream.adapter",
        "com.shop4me.productdatastream.application",
        "com.shop4me.productdatastream.domain"
}, importOptions = ImportOption.DoNotIncludeTests.class)
class Shop4MeProductDataStreamOnionArchitectureTest {

    @ArchTest
    public static final ArchRule ONION_ARCHITECTURE_TEST = Architectures.onionArchitecture()
            .withOptionalLayers(true)
            .adapter("inbound", "com.shop4me.productdatastream.adapter.inbound..")
            .applicationServices("com.shop4me.productdatastream.application..")
            .domainServices("com.shop4me.productdatastream.domain.port..")
            .domainServices("com.shop4me.productdatastream.domain.service..")
            .domainModels("com.shop4me.productdatastream.domain.model..")
            .because("I love my pierogi with onion");

    @ArchTest
    public static final ArchRule SPRING_DATA_TRANSACTION_ANNOTATION_ONLY_IN_PORT_PERSISTING = ArchRuleDefinition.noMethods()
            .that().areDeclaredInClassesThat()
            .resideOutsideOfPackage("com.shop4me.productdatastream.domain.port.persisting.repositories..")
            .should().beAnnotatedWith(Transactional.class)
            .orShould().beAnnotatedWith(Modifying.class)
            .because("Interface in this packed are responsable for Spring data transaction");

    @ArchTest
    public static  final ArchRule NO_BEANS_DECLARATION_IN_DOMAIN = ArchRuleDefinition.noClasses()
            .that().resideInAnyPackage("com.shop4me.productdatastream.domain..")
            .should().beAnnotatedWith(Component.class)
            .orShould().beAnnotatedWith(Service.class)
            .orShould().beAnnotatedWith(Repository.class)
            .because("I want to keep clean domain from spring beans injection");

    @ArchTest
    public static final ArchRule NO_DEPENDENCY_INJECTION_IN_DOMAIN = ArchRuleDefinition.noMethods()
            .that().areDeclaredInClassesThat()
            .resideInAPackage("com.shop4me.productdatastream.domain..")
            .should().beAnnotatedWith(Autowired.class);
}
