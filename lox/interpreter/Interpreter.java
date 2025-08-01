package lox.interpreter;

import lox.Lox;
import lox.util.RuntimeError;
import lox.scanner.Token;
import lox.ast.Expr;
import lox.ast.Stmt;
import lox.runtime.Environment;
import lox.runtime.LoxClass;
import lox.runtime.LoxFunction;
import lox.runtime.LoxInstance;
import lox.util.LoxCallable;
import lox.util.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

    public final Environment globals = new Environment();
    private Environment environment = globals;
    private final Map<Expr, Integer> locals = new HashMap<>();

    public Interpreter() {
        globals.define("clock", new LoxCallable() {
            @Override
            public int arity() {return 0;}

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double) System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString() {return "<native fn>"; }
        });
    }

    public void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        }catch (RuntimeError e) {
            Lox.runtimeError(e);
        }
    }

    public Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    public void resolve(Expr expr, int depth) {
        locals.put(expr,depth);
    }

    public Void visitBlockStmt(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    public Void visitClassDefStmt(Stmt.ClassDef stmt) {
        environment.define(stmt.name.lexeme, null);
        Object superclass = null;
        Map<String, LoxFunction> methods = new HashMap<>();
        Map<String, LoxFunction> staticMethods = new HashMap<>();

        if (stmt.superclass != null) {
            superclass = evaluate(stmt.superclass);
            if (!(superclass instanceof LoxClass)) {
                throw new RuntimeError(stmt.superclass.name, "Superclass must be a lox Class.");
            }
            environment = new Environment(environment);
            environment.define("super", superclass);
        }

        for(Stmt.Function method : stmt.methods) {
            LoxFunction function = new LoxFunction(method, environment, method.name.lexeme.equals("init"));

            methods.put(method.name.lexeme, function);
        }

        for(Stmt.Function method : stmt.staticMethods) {
            LoxFunction function = new LoxFunction(method, environment, method.name.lexeme.equals("init"));

            staticMethods.put(method.name.lexeme, function);
        }

        if (superclass != null) {
            environment = environment.enclosing;
        }

        LoxClass loxClass = new LoxClass(stmt.name.lexeme, (LoxClass)superclass, methods, staticMethods);

        environment.assign(stmt.name, loxClass);
        return null;
    }

    public Void visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    public Void visitPrintStmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    public Void visitVarStmt(Stmt.Var stmt) {
        Object value = null;

        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }

        environment.define(stmt.name.lexeme, value);

        return null;
    }

    public Void visitIfConditionStmt(Stmt.IfCondition stmt) {
        if (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitWhileLoopStmt(Stmt.WhileLoop whileloop) {
        while(isTruthy(evaluate(whileloop.condition))) {
            execute(whileloop.body);
        }
        return null;
    }

    public Object visitLogicalExpr(Expr.Logical expr) {
        Object left = evaluate(expr.left);

        if (expr.operator.type == TokenType.OR) {
            if (isTruthy(left)) return left;
        } else {
            if (!isTruthy(left)) return left;
        }

        return evaluate(expr.right);
    }

    public Object visitAssignExpr(Expr.Assign expr) {
        Object value = evaluate(expr.value);
        Integer distance = locals.get(expr);

        if (distance != null) {
            environment.assignAt(distance, expr.name, value);
        } else {
            globals.assign(expr.name, value);
        }

        return value;
    }

    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
        return lookUpVariable(expr.name, expr);
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }


    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(right);
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double)right;
        }

        return null;
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left - (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }
                if (left instanceof String ) {
                    return left + right.toString();
                } else if (right instanceof String ) {
                    return right + left.toString();
                }

                throw new RuntimeError(expr.operator, "Operands don't match. Left: " + left + ", Right: " + right + ", Types: " + (left == null ? "null" : left.getClass().getSimpleName()) + ", " + (right == null ? "null" : right.getClass().getSimpleName()));
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                if ((Double) right == 0) {
                    throw new RuntimeError(expr.operator, "Cannot divide by zero.");
                }
                return (double) left / (double) right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double) left * (double) right;
            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left >= (double) right;
            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left <= (double) right;
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
        }
        return null;
    }

    @Override
    public Object visitCallExpr(Expr.Call expr) {
        Object callee = evaluate(expr.callee);

        if (!(callee instanceof LoxCallable)) {
            throw new RuntimeError(expr.paren, "Can only call functions and classes.");
        }

        List<Object> arguments = new ArrayList<>();

        for (Expr argument : expr.arguments) {
            arguments.add(evaluate(argument));
        }

        LoxCallable function = (LoxCallable)callee;

        if (arguments.size() != function.arity()) {
            throw new RuntimeError(expr.paren, "Expected " + function.arity() + " but got " + arguments.size() + " arguments.");
        }

        return function.call(this, arguments);
    }

    @Override
    public Object visitGetExpr(Expr.Get expr) {
        Object object = evaluate(expr.object);

        if (object instanceof LoxInstance) {
            return ((LoxInstance) object).get(expr.name);
        }

        if (object instanceof LoxClass) {
            LoxFunction staticMethod = ((LoxClass) object).findStaticMethod(expr.name.lexeme);
            if (staticMethod != null) {
                return staticMethod;
            }
        }

        throw new RuntimeError(expr.name, "Can only get functions and classes.");
    }

    @Override
    public Object visitSetExpr(Expr.Set expr) {
        Object object = evaluate(expr.object);
        if (!(object instanceof LoxInstance)) {
            throw new RuntimeError(expr.name, "Only instances have fields.");
        }

        Object value = evaluate(expr.value);

        ((LoxInstance)object).set(expr.name, value);
        return value;
    }

    @Override
    public Object visitLoxSuperExpr(Expr.LoxSuper expr) {
        int distance = locals.get(expr);
        LoxClass superclass = (LoxClass)environment.getAt(distance, "super");
        LoxInstance object = (LoxInstance)environment.getAt(distance - 1, "this");
        LoxFunction method = superclass.findMethod(expr.method.lexeme);

        if (method == null) {
            throw new RuntimeError(expr.method, "Undefined property " + expr.method.lexeme);
        }
        return method.bind(object);
    }

    @Override
    public Object visitLoxThisExpr(Expr.LoxThis expr) {
        return lookUpVariable(expr.keyword, expr);
    }

    @Override
    public Void visitFunctionStmt(Stmt.Function stmt) {
         LoxFunction function = new LoxFunction(stmt, environment, false);

         environment.define(stmt.name.lexeme, function);

         return null;
    }

    @Override
    public Void visitReturnStmtStmt(Stmt.ReturnStmt stmt) {
        Object value = null;
        if (stmt.value != null) value = evaluate(stmt.value);

        throw new Return(value);
    }

    @Override
    public Object visitTernaryExpr(Expr.Ternary expr) {
        Object condition = evaluate(expr.condition);
        if (isTruthy(condition)) {
            return expr.trueCondition;
        }
        return expr.falseCondition;
    }

    private Object lookUpVariable(Token name, Expr expr) {
        Integer distance = locals.get(expr);
        if (distance != null) {
            return environment.getAt(distance, name.lexeme);
        } else {
            return globals.get(name);
        }
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Operands must be a numbers.");
    }

    private boolean isEqual(Object left, Object right) {
        if (left == null && right == null) return true;
        if (left == null) return false;
        return left.equals(right);
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) {
            return (boolean)object;
        }
        return true;
    }

    public String stringify(Object value) {
        if (value == null) return "nil";

        if (value instanceof Double) {
            String text = value.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return value.toString();
    }

    public void executeBlock(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;
            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }

}
