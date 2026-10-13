import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.transform.stc.ExtensionMethodNode

/*
 * Commands are resolved by Groovy's normal extension-method machinery.
 * This hook verifies that calls resolved to an extension method inside
 * this DSL come from the Commands provider, rather than maintaining a
 * hard-coded list of command names.
 */
onMethodSelection { expression, target ->
    if (expression instanceof MethodCallExpression &&
        target instanceof ExtensionMethodNode &&
        target.extensionMethod.declaringClass.name == 'demo.Commands') {
        // The command is backed by a method in demo.Commands.
        // Its real signature is used by @CompileStatic for static dispatch.
    }
}
