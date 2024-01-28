package zsu.ksp.ex

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSDeclaration
import zsu.ksp.ex.impl.ExtResolverImpl

interface ExtResolver : Resolver {
    fun allDeclarationsWithDependencies(): Sequence<KSDeclaration>
}

val Resolver.ext: ExtResolver get() = ExtResolverImpl(this)
