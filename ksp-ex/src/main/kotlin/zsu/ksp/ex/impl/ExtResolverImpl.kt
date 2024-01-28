package zsu.ksp.ex.impl

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.impl.ResolverImpl
import com.google.devtools.ksp.processing.impl.toKSDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import org.jetbrains.kotlin.descriptors.MemberDescriptor
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter
import zsu.ksp.ex.ExtResolver

/** very time-costing action, be careful to use */
internal class ExtResolverImpl(resolver: Resolver) : ExtResolver, Resolver by resolver {
    private val resolver = resolver as ResolverImpl

    /** all kotlin declarations in itself and dependencies */
    override fun allDeclarationsWithDependencies(): Sequence<KSDeclaration> {
        val module = resolver.module
        return getDeclarationsFromPackage(module, hashSetOf())
    }

    private fun getDeclarationsFromPackage(
        moduleDescriptor: ModuleDescriptor,
        visited: HashSet<ModuleDescriptor>,
    ): Sequence<KSDeclaration> = sequence {
        if (moduleDescriptor in visited) return@sequence
        visited += moduleDescriptor
        // todo adds children unavailable currently
//        moduleDescriptor.allDependencyModules.forEach { childModule ->
//            yieldAll(getDeclarationsFromPackage(childModule, visited))
//        }
        // adds self
        val packageNames = moduleDescriptor.allPackageNames()
        yieldAll(packageNames.flatMap {
            getDeclarationsFromPackage(moduleDescriptor, it)
        })
    }

    private val noPackageFilter = DescriptorKindFilter.ALL.withoutKinds(DescriptorKindFilter.PACKAGES_MASK)
    private fun getDeclarationsFromPackage(
        moduleDescriptor: ModuleDescriptor,
        packageName: FqName,
    ): Sequence<KSDeclaration> {
        return moduleDescriptor.getPackage(packageName)
            .memberScope.getContributedDescriptors(noPackageFilter)
            .asSequence()
            .mapNotNull { (it as? MemberDescriptor)?.toKSDeclaration() }
    }
}

private fun ModuleDescriptor.allPackageNames(): Sequence<FqName> = sequence {
    val packageNames = packageNames(FqName.ROOT)
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
