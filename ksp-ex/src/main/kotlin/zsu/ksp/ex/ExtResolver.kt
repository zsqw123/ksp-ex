package zsu.ksp.ex

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.impl.ResolverImpl
import com.google.devtools.ksp.processing.impl.toKSDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.MemberDescriptor
import org.jetbrains.kotlin.descriptors.impl.DeclarationDescriptorVisitorEmptyBodies

/** very time-costing action, be careful to use */
class ExtResolver(resolver: Resolver) {
    private val resolver = resolver as ResolverImpl

    /** all kotlin declarations in itself and dependencies */
    fun allDeclarations(): Sequence<KSDeclaration> = sequence {
        val allDescriptors = arrayListOf<MemberDescriptor>()
        val visitorImpl = DeclarationVisitorImpl {
            if (it is MemberDescriptor) allDescriptors += it
        }
        resolver.module.accept(visitorImpl, Unit)
        val all = allDescriptors.asSequence().map {
            it.toKSDeclaration()
        }
        yieldAll(all)
    }
}

private class DeclarationVisitorImpl(
    private val onNewItem: (descriptor: DeclarationDescriptor) -> Unit,
) : DeclarationDescriptorVisitorEmptyBodies<Any, Any>() {
    override fun visitDeclarationDescriptor(descriptor: DeclarationDescriptor?, data: Any?): Any? {
        onNewItem(descriptor ?: return null)
        return null
    }
}