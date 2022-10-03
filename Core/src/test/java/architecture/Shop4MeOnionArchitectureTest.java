package architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = {
        "com.shop4me.core.adapter",
        "com.shop4me.core.application",
        "com.shop4me.core.domain"
}, importOptions = ImportOption.DoNotIncludeTests.class)
public class Shop4MeOnionArchitectureTest {

    @ArchTest
    public static final ArchRule ONION_ARCHITECTURE_TEST = Architectures.onionArchitecture()
            .withOptionalLayers(true)
            .adapter("inbound", "com.shop4me.core.adapter.inbound..")
            .adapter("outbound", "com.shop4me.core.adapter.outbound..")
            .applicationServices("com.shop4me.core.application..")
            .domainServices("com.shop4me.core.domain.port..")
            .domainServices("com.shop4me.core.domain.service..")
            .domainModels("com.shop4me.core.domain.model..")
            .because("I love my pierogi with onion");
}
