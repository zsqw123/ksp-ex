package zsu.ksp.ex

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSDeclaration
import zsu.ksp.ex.impl.ExtResolverImpl

interface ExtResolver : Resolver {
    /**
     * All kotlin declarations in itself and their direct dependencies.
     * Might be very time-costing. Not support for incremental processing now.
     *
     * @param rootPackage Which root directory to start looking at, defaulting to the root package.
     *  e.g. [kotlin] package will analyze all package inside of `kotlin` package, such like:
     *  [kotlin.reflect], [kotlin.collections], [kotlin.jvm.functions] and so on.
     *  You can get better performance if you use more specified package names.
     * @param nameFilter full qualified name filter for filter needed declarations.
     */
    fun allDeclarationsWithDependencies(
        rootPackage: String = "",
        nameFilter: (String) -> Boolean = nameFilterAlwaysTrue,
    ): Sequence<KSDeclaration>
}

internal val nameFilterAlwaysTrue: (String) -> Boolean = { true }

val Resolver.ext: ExtResolver get() = ExtResolverImpl(this)
