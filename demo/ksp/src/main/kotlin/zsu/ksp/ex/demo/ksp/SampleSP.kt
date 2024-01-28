package zsu.ksp.ex.demo.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import zsu.ksp.ex.ExtResolver
import zsu.ksp.ex.demo.api.SampleAnno

class SampleSPP : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return SampleSP(environment)
    }
}

class SampleSP(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {
    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val wrap = ExtResolver(resolver)
        val sampleResult = wrap.allDeclarations()
            .filter { it.isAnnotationPresent(SampleAnno::class) }
            .map { it.qualifiedName }
        environment.logger.warn(sampleResult.toString())
        return emptyList()
    }
}
