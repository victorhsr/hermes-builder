package io.github.victorhsr.hermes.maven

import io.github.victorhsr.hermes.core.HermesRunner
import io.github.victorhsr.hermes.core.HermesRunnerFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.MockedConstruction
import org.mockito.MockedStatic
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

@ExtendWith(MockitoExtension::class)
class DSLProcessorTest {

    @Mock
    private lateinit var elements: Set<TypeElement>

    @Mock
    private lateinit var roundEnv: RoundEnvironment

    @Mock
    private lateinit var hermesRunner: HermesRunner

    private lateinit var elementDefinitionsBuilderConstMock: MockedConstruction<ElementDefinitionsBuilder>
    private lateinit var classInfoBuilderConstMock: MockedConstruction<ClassInfoBuilder>

    private lateinit var hermesRunnerFactoryMockStatic: MockedStatic<HermesRunnerFactory>

    fun setUp() {
        this.hermesRunnerFactoryMockStatic = mockStatic(HermesRunnerFactory::class.java)
        this.hermesRunnerFactoryMockStatic.`when`<HermesRunner> { HermesRunnerFactory.create() }
            .thenReturn(this.hermesRunner)
        this.mockElementDefinitionsBuilder()
        this.mockClassInfoBuilder()
    }

    fun shutDown() {
        this.elementDefinitionsBuilderConstMock.close()
        this.hermesRunnerFactoryMockStatic.close()
        this.classInfoBuilderConstMock.close()
    }

    private fun mockElementDefinitionsBuilder() {
        this.elementDefinitionsBuilderConstMock = mockConstruction(ElementDefinitionsBuilder::class.java)
        { mock, _ -> }
    }

    private fun mockClassInfoBuilder() {
        this.classInfoBuilderConstMock = mockConstruction(ClassInfoBuilder::class.java)
        { mock, _ -> }
    }
}