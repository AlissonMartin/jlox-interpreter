package lox;

import java.util.List;

abstract class Stmt {

    interface Visitor<R> {
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

    static class Block extends Stmt {

        final List<Stmt> statements;
        Block(List<Stmt> statements) {
            this.statements = statements;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }
    }

    static class ClassDef extends Stmt {

        final Token name;
        final List<Stmt.Function> methods;
        ClassDef(Token name, List<Stmt.Function> methods) {
            this.name = name;
            this.methods = methods;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitClassDefStmt(this);
        }
    }

    static class Expression extends Stmt {

        final Expr expression;
        Expression(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }

    static class Function extends Stmt {

        final Token name;
        final List<Token> params;
        final List<Stmt> body;
        Function(Token name, List<Token> params, List<Stmt> body) {
            this.name = name;
            this.params = params;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionStmt(this);
        }
    }

    static class IfCondition extends Stmt {

        final Expr condition;
        final Stmt thenBranch;
        final Stmt elseBranch;
        IfCondition(Expr condition, Stmt thenBranch, Stmt elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfConditionStmt(this);
        }
    }

    static class Print extends Stmt {

        final Expr expression;
        Print(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }

    static class ReturnStmt extends Stmt {

        final Token keyword;
        final Expr value;
        ReturnStmt(Token keyword, Expr value) {
            this.keyword = keyword;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitReturnStmtStmt(this);
        }
    }

    static class Var extends Stmt {

        final Token name;
        final Expr initializer;
        Var(Token name, Expr initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }
    }

    static class WhileLoop extends Stmt {

        final Expr condition;
        final Stmt body;
        WhileLoop(Expr condition, Stmt body) {
            this.condition = condition;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileLoopStmt(this);
        }
    }


    abstract <R> R accept(Visitor<R> visitor);
}
