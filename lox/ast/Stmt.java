package lox.ast;

import lox.scanner.Token;

import java.util.List;

public abstract class Stmt {

    public interface Visitor<R> {
        R visitBlockStmt(Block block);
        R visitClassDefStmt(ClassDef classdef);
        R visitExpressionStmt(Expression expression);
        R visitFunctionStmt(Function function);
        R visitIfConditionStmt(IfCondition ifcondition);
        R visitPrintStmt(Print print);
        R visitReturnStmtStmt(ReturnStmt returnstmt);
        R visitVarStmt(Var var);
        R visitWhileLoopStmt(WhileLoop whileloop);
}

    public static class Block extends Stmt {

        public final List<Stmt> statements;
        public Block(List<Stmt> statements) {
            this.statements = statements;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }
    }

    public static class ClassDef extends Stmt {

        public final Token name;
        public final Expr.Variable superclass;
        public final List<Stmt.Function> methods;
        public final List<Stmt.Function> staticMethods;
        public ClassDef(Token name, Expr.Variable superclass, List<Stmt.Function> methods, List<Stmt.Function> staticMethods) {
            this.name = name;
            this.superclass = superclass;
            this.methods = methods;
            this.staticMethods = staticMethods;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitClassDefStmt(this);
        }
    }

    public static class Expression extends Stmt {

        public final Expr expression;
        public Expression(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }

    public static class Function extends Stmt {

        public final Token name;
        public final List<Token> params;
        public final List<Stmt> body;
        public Function(Token name, List<Token> params, List<Stmt> body) {
            this.name = name;
            this.params = params;
            this.body = body;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionStmt(this);
        }
    }

    public static class IfCondition extends Stmt {

        public final Expr condition;
        public final Stmt thenBranch;
        public final Stmt elseBranch;
        public IfCondition(Expr condition, Stmt thenBranch, Stmt elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfConditionStmt(this);
        }
    }

    public static class Print extends Stmt {

        public final Expr expression;
        public Print(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }

    public static class ReturnStmt extends Stmt {

        public final Token keyword;
        public final Expr value;
        public ReturnStmt(Token keyword, Expr value) {
            this.keyword = keyword;
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitReturnStmtStmt(this);
        }
    }

    public static class Var extends Stmt {

        public final Token name;
        public final Expr initializer;
        public Var(Token name, Expr initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }
    }

    public static class WhileLoop extends Stmt {

        public final Expr condition;
        public final Stmt body;
        public WhileLoop(Expr condition, Stmt body) {
            this.condition = condition;
            this.body = body;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileLoopStmt(this);
        }
    }


    public abstract <R> R accept(Visitor<R> visitor);
}
