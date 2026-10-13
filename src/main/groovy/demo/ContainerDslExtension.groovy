import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ClassExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression

/*
 * Resolve explicit receiver calls against static methods in Commands.
 * Selection considers receiver type, argument count, and each argument's
 * inferred static type. No DSL name-to-method table is maintained.
 *
 * Rewriting to Commands.method(receiver, args...) makes the selected target
 * an ordinary static call and avoids accidentally choosing a same-named DGM
 * extension method.
 */
beforeMethodCall { call ->
    if (!(call instanceof MethodCallExpression) ||
        call.implicitThis ||
        call.methodAsString == null) {
        return false
    }

    def receiverType = getType(call.objectExpression)
    if (receiverType.name != 'demo.Container') {
        return false
    }

    def arguments = call.arguments instanceof ArgumentListExpression
        ? call.arguments.expressions
        : [call.arguments]

    def commands = classNodeFor('demo.Commands')
    def candidates = commands.getMethods(call.methodAsString).findAll { method ->
        if (!method.isStatic() || method.parameters.length != arguments.size() + 1) {
            return false
        }

        def receiverParameter = method.parameters[0].type
        if (receiverParameter.name != 'demo.Container') {
            return false
        }

        def actualTypes = arguments.collect { arg -> getType(arg) }
        def formalTypes = method.parameters.drop(1).collect { parameter -> parameter.type }
        for (int i = 0; i < actualTypes.size(); i++) {
            if (!sameOrAssignable(actualTypes[i], formalTypes[i])) {
                return false
            }
        }
        return true
    }

    if (candidates.size() == 1) {
        def selected = candidates[0]
        def rewrittenArguments = new ArgumentListExpression()
        rewrittenArguments.addExpression(call.objectExpression)
        arguments.each { rewrittenArguments.addExpression(it) }

        call.setObjectExpression(new ClassExpression(commands))
        call.setMethod(new ConstantExpression(selected.name))
        call.setArguments(rewrittenArguments)
        call.setImplicitThis(false)
    } else if (candidates.size() > 1) {
        addStaticTypeError("Ambiguous DSL call: multiple Commands overloads match", call)
    }
    return false
}

/*
 * This example uses exact types for overload discrimination and permits
 * basic reference assignability and primitive/wrapper pairing. A production
 * resolver should use Groovy's complete overload-resolution rules.
 */
boolean sameOrAssignable(actual, formal) {
    if (actual == formal || actual.name == formal.name) {
        return true
    }
    if (actual.isPrimitive() && ClassHelper.getWrapper(actual) == formal) {
        return true
    }
    if (formal.isPrimitive() && ClassHelper.getWrapper(formal) == actual) {
        return true
    }
    return actual.isDerivedFrom(formal) || actual.implementsInterface(formal)
}
