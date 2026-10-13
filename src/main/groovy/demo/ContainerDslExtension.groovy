import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ClassExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression

/*
 * Route calls on Container to same-named static methods in Commands.
 *
 * This runs before normal method resolution. Container.each would otherwise
 * be eligible for Groovy's built-in DGM each(Iterable, Closure). We discover
 * the DSL method by name and receiver parameter; there is no command-name map.
 *
 * Rewriting the call as Commands.each(container, ...) lets @CompileStatic
 * compile a normal static invocation.
 */
beforeMethodCall { call ->
    if (call instanceof MethodCallExpression &&
        !call.implicitThis &&
        call.methodAsString != null) {

        def receiverType = getType(call.objectExpression)
        if (receiverType.name == 'demo.Container') {
            def commands = classNodeFor('demo.Commands')
            def candidates = commands.getMethods(call.methodAsString).findAll { method ->
                method.isStatic() &&
                    method.parameters.length > 0 &&
                    method.parameters[0].type.name == 'demo.Container'
            }

            if (candidates.size() == 1) {
                def receiver = call.objectExpression
                def originalArguments = call.arguments
                def arguments = new ArgumentListExpression()
                arguments.addExpression(receiver)
                if (originalArguments instanceof ArgumentListExpression) {
                    originalArguments.expressions.each { arguments.addExpression(it) }
                } else {
                    arguments.addExpression(originalArguments)
                }

                call.setObjectExpression(new ClassExpression(commands))
                call.setMethod(new ConstantExpression(call.methodAsString))
                call.setArguments(arguments)
                call.setImplicitThis(false)
            }
        }
    }
    return false
}
