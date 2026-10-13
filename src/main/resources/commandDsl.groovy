import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.transform.stc.ExtensionMethodNode

/*
 * Commands are resolved by Groovy's normal extension-method machinery.
 * This hook observes the selected target and recognizes methods supplied
 * by Commands without maintaining a command-name-to-method table.
 */
onMethodSelection { expression, target ->
    if (expression instanceof MethodCallExpression &&
        target instanceof ExtensionMethodNode &&
        target.extensionMethodNode.declaringClass.name == 'demo.Commands') {
        // Groovy has selected a concrete method from demo.Commands.
        // @CompileStatic can use that method's real signature and target.
    }
}
