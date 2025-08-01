package lox.runtime;

import lox.interpreter.Interpreter;
import lox.util.LoxCallable;

import java.util.List;
import java.util.Map;

public class LoxClass implements LoxCallable {

    final String name;
    private Map<String, LoxFunction> methods;
    private Map<String, LoxFunction> staticMethods;
    final LoxClass superclass;

    public LoxClass(String name, LoxClass superclass, Map<String, LoxFunction> methods, Map<String, LoxFunction> staticMethods) {
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
        this.staticMethods = staticMethods;
    }

    public LoxFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }

        if (superclass != null) {
            return superclass.findMethod(name);
        }
        return null;
    }

    public LoxFunction findStaticMethod(String name) {
        if (staticMethods.containsKey(name)) {
            return staticMethods.get(name);
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        LoxInstance instance = new LoxInstance(this);
        LoxFunction initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }

    @Override
    public int arity() {
        LoxFunction initializer = findMethod("init");

        if (initializer == null) return 0;
        return initializer.arity();
    }

}
