package lox.util;

import lox.interpreter.Interpreter;

import java.util.List;

public interface LoxCallable {
    int arity(); // Number of arguments.
    Object call(Interpreter interpreter, List<Object> arguments);
}
