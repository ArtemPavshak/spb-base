package edu.pro.spbbase;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import lombok.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@SpringBootTest
class SpbBaseArchitectureTests {

    private JavaClasses applicationClasses;

    @BeforeEach
    void init() {
        applicationClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("edu.pro.spbbase");
    }

    @Test
    void shouldFollowLayerArchitecture() {
        layeredArchitecture()
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Repository").definedBy("..repository..")
                //
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
                //
                .check(applicationClasses);
    }

    @Test
    void servicesShouldNotDependOnController() {
        noClasses()
                .that().resideInAPackage("..service..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..controller..")
                .because("out of arch rules")
                .check(applicationClasses);
    }

    @Test
    void repositoryShouldNotDependOnController() {
        noClasses()
                .that().resideInAPackage("..repository..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..controller..")
                .because("out of arch rules")
                .check(applicationClasses);
    }

    @Test
    void controllerShouldNotDependOnRepository() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..repository..")
                .because("out of arch rules")
                .check(applicationClasses);
    }

    @Test
    void controllerClassesShouldBeNamedXController() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .haveSimpleNameEndingWith("Controller")
                .check(applicationClasses);

    }

    @Test
    void ControllerClassesShouldBeNamedItemX() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .haveSimpleNameStartingWith("Item")
                .check(applicationClasses);
    }

    @Test
    void ServiceClassesShouldBeNamedItemX() {
        classes()
                .that().resideInAPackage("..service..")
                .should()
                .haveSimpleNameStartingWith("Item")
                .check(applicationClasses);
    }

    @Test
    void RepositoryClassesShouldBeNamedItemX() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .haveSimpleNameStartingWith("Item")
                .check(applicationClasses);
    }

    @Test
    void ModelClassesShouldBeNamedItemX() {
        classes()
                .that().resideInAPackage("..model..")
                .should()
                .haveSimpleNameStartingWith("Item")
                .check(applicationClasses);
    }

    @Test
    void repositoryClassesShouldNotBeAnnotatedByController() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .notBeAnnotatedWith(Controller.class)
                .check(applicationClasses);
    }

    @Test
    void modelClassesShouldBeAnnotatedWithDocument() {
        classes()
                .that().resideInAPackage("..model..")
                .should().beAnnotatedWith(Document.class)
                .check(applicationClasses);
    }
    @Test
    void repositoryClassesShouldBeNamedXController() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .haveSimpleNameEndingWith("Repository")
                .check(applicationClasses);
    }

    @Test
    void serviceClassesShouldBeNamedXController() {
        classes()
                .that().resideInAPackage("..service..")
                .should()
                .haveSimpleNameEndingWith("Service")
                .check(applicationClasses);

    }


    @Test
    void controllerClassesShouldBeAnnotatedBy() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(RestController.class)
                .andShould()
                .beAnnotatedWith(RequestMapping.class)
                .check(applicationClasses);
    }

    @Test
    void repositoryClassesShouldBeAnnotatedBy() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .beAnnotatedWith(Repository.class)
                .check(applicationClasses);
    }
    @Test
    void serviceClassesShouldBeAnnotatedBy() {
        classes()
                .that().resideInAPackage("..service..")
                .should()
                .beAnnotatedWith(Service.class)
                .check(applicationClasses);
    }

    @Test
    void shouldNotUseFieldsAutowired() {
        noFields()
                .should()
                .beAnnotatedWith(Autowired.class)
                .check(applicationClasses);
    }

    @Test
    void serviceMethodsShouldBePublic() {
        methods()
                .that().areDeclaredInClassesThat().resideInAPackage("..service..")
                .should().bePublic()
                .check(applicationClasses);
    }

    @Test
    void RepositoryMethodsShouldBePublic() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().bePublic()
                .check(applicationClasses);
    }

    @Test
    void controllerClassesShouldBeAnnotatedWithControllerAdvice() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(ControllerAdvice.class)
                .check(applicationClasses);
    }



}
