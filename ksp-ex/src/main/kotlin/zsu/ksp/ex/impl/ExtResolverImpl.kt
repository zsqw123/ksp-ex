package zsu.ksp.ex.impl

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.impl.ResolverImpl
import com.google.devtools.ksp.processing.impl.toKSDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import org.jetbrains.kotlin.descriptors.MemberDescriptor
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter
import zsu.ksp.ex.ExtResolver
import zsu.ksp.ex.nameFilterAlwaysTrue

private typealias FqNameFilter = (FqName) -> Boolean

internal class ExtResolverImpl(resolver: Resolver) : ExtResolver, Resolver by resolver {
    private val resolver = resolver as ResolverImpl

    override fun allDeclarationsWithDependencies(
        rootPackage: String,
        nameFilter: (String) -> Boolean,
    ): Sequence<KSDeclaration> {
        val module = resolver.module
        val fqNameFilter = if (nameFilter === nameFilterAlwaysTrue) null else { fqName: FqName ->
            nameFilter(fqName.asString())
        }
        return getDeclarationsFromPackage(
            FqName(rootPackage), module, hashSetOf(), fqNameFilter
        )
    }

    private fun getDeclarationsFromPackage(
        rootPackage: FqName,
        moduleDescriptor: ModuleDescriptor,
        visited: HashSet<ModuleDescriptor>,
        nameFilter: FqNameFilter?,
    ): Sequence<KSDeclaration> = sequence {
        if (moduleDescriptor in visited) return@sequence
        visited += moduleDescriptor
        // todo adds children unavailable currently
//        moduleDescriptor.allDependencyModules.forEach { childModule ->
//            yieldAll(getDeclarationsFromPackage(childModule, visited))
//        }
        // adds self
        val packageNames = moduleDescriptor.allPackageNames(rootPackage)
        yieldAll(packageNames.flatMap {
            getDeclarationsFromPackage(moduleDescriptor, it, nameFilter)
        })
    }

    private val noPackageFilter = DescriptorKindFilter.ALL.withoutKinds(DescriptorKindFilter.PACKAGES_MASK)
    private fun getDeclarationsFromPackage(
        moduleDescriptor: ModuleDescriptor,
        packageName: FqName,
        nameFilter: FqNameFilter?,
    ): List<KSDeclaration> {
        val descriptors = moduleDescriptor.getPackage(packageName)
            .memberScope.getContributedDescriptors(noPackageFilter)
            // use nameFilter if it has.
            .filter { it is MemberDescriptor && (nameFilter == null || nameFilter(it.fqNameSafe)) }
        return descriptors.map { (it as MemberDescriptor).toKSDeclaration() }
    }
}

private fun ModuleDescriptor.allPackageNames(rootPackage: FqName): Sequence<FqName> = sequence {
    val packageNames = packageNames(rootPackage)
    yieldAll(packageNames)
}

private val noNameFilter: (Name) -> Boolean = { true }
private fun ModuleDescriptor.packageNames(
    currentPackage: FqName
): Sequence<FqName> = sequence {
    yield(currentPackage)
    val packages = getSubPackagesOf(currentPackage, noNameFilter)
    packages.forEach {
        yieldAll(packageNames(it))
    }
}
