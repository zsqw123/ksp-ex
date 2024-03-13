package zsu.ksp.ex

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSDeclaration
import org.jetbrains.kotlin.name.FqName
import zsu.ksp.ex.impl.ExtResolverImpl

interface ExtResolver : Resolver {
    /**
     * All kotlin declarations in itself and their direct dependencies.
     * Might be very time-costing. Not support for incremental processing now.
     *
     * @param rootPackage Which root directory to start looking at, defaulting to the root package.
     *  e.g. [kotlin] package will analyze all package inside of `kotlin` package, such like:
     *  [kotlin.reflect], [kotlin.jvm], [kotlin.collections] and so on.
     *  You can get better performance if you use more specified package names.
     */
    fun allDeclarationsWithDependencies(rootPackage: FqName = FqName.ROOT): Sequence<KSDeclaration>
}

val Resolver.ext: ExtResolver get() = ExtResolverImpl(this)
