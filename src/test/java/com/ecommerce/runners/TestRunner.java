package com.ecommerce.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * Cucumber Test Runner using JUnit Platform Suite.
 *
 * Run specific tags:
 *   mvn test -Dcucumber.filter.tags="@smoke"
 *   mvn test -Dcucumber.filter.tags="@ui and @login"
 *   mvn test -Dcucumber.filter.tags="@api"
 *   mvn test -Dcucumber.filter.tags="@regression and not @api"
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("/features")
@ConfigurationParameter(
    key = PLUGIN_PROPERTY_NAME,
    value = "pretty, " +
            "html:target/cucumber-reports/cucumber.html, " +
            "json:target/cucumber-reports/cucumber.json, " +
            "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)
@ConfigurationParameter(
    key = GLUE_PROPERTY_NAME,
    value = "com.ecommerce.stepdefinitions"
)
@ConfigurationParameter(
    key = FEATURES_PROPERTY_NAME,
    value = "src/test/resources/features/api_login.feature"
)
@ConfigurationParameter(
    key = FILTER_TAGS_PROPERTY_NAME,
    value = "@TestAPI"
)
@ConfigurationParameter(
    key = SNIPPET_TYPE_PROPERTY_NAME,
    value = "camelcase"
)
public class TestRunner {
    // This class is intentionally empty.
    // All configuration is done via annotations.
}
