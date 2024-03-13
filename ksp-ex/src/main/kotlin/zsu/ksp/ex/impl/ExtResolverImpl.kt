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

internal class ExtResolverImpl(resolver: Resolver) : ExtResolver, Resolver by resolver {
    private val resolver = resolver as ResolverImpl

    override fun allDeclarationsWithDependencies(rootPackage: FqName): Sequence<KSDeclaration> {
        val module = resolver.module
        return getDeclarationsFromPackage(rootPackage, module, hashSetOf())
    }

    private fun getDeclarationsFromPackage(
        rootPackage: FqName,
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
        val packageNames = moduleDescriptor.allPackageNames(rootPackage)
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
