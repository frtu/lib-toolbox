package com.github.frtu.kotlin.spring.tool.scanner

import com.github.frtu.kotlin.serdes.json.ext.objToJsonNode
import com.github.frtu.kotlin.serdes.json.ext.toJsonObj
import com.github.frtu.kotlin.spring.tool.annotation.WeatherToolAnnotation
import io.kotlintest.matchers.collections.shouldNotBeEmpty
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import sample.tool.SampleToolConfig
import sample.tool.model.WeatherForecastInputParameter
import sample.tool.model.WeatherInfoMultiple

class ToolBuilderFromAnnotationScannerTest {
    private val applicationContextRunner = ApplicationContextRunner()

    @Test
    fun annotatedBeans() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        // Init var
        val location = "Glasgow, Scotland"
        val unit = sample.tool.model.Unit.celsius

        val parameter = WeatherForecastInputParameter(
            location = location,
            unit = unit,
            numberOfDays = 5,
        )


        applicationContextRunner
            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            .withUserConfiguration(
                SampleBeanConfig::class.java,
                ToolBuilderFromAnnotationScanner::class.java
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                val toolBuilderFromAnnotationScanner = context.getBean(ToolBuilderFromAnnotationScanner::class.java)
                val tools = toolBuilderFromAnnotationScanner.annotatedBeans()
                tools.shouldNotBeNull()
                tools.shouldNotBeEmpty()
                with(tools[0]) {
                    id.value shouldBe WeatherToolAnnotation.TOOL_NAME
                    description shouldBe WeatherToolAnnotation.TOOL_DESCRIPTION
                    runBlocking {
                        val result = execute(parameter.objToJsonNode()).toJsonObj(WeatherInfoMultiple::class.java)
                        with(result) {
                            shouldNotBeNull()
                            this.location shouldBe parameter.location
                            this.unit shouldBe parameter.unit
                            this.numberOfDays shouldBe parameter.numberOfDays
                            this.temperature.shouldNotBeNull()
                            this.forecast.shouldNotBeEmpty()
                        }
                    }
                }
            }
    }
}

@Configuration
@Import(SampleToolConfig::class)
@ComponentScan(basePackageClasses = [WeatherToolAnnotation::class])
class SampleBeanConfig