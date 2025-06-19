package lox;

import java.util.List;

abstract class Stmt {

    interface Visitor<R> {
        R visitBlockStmt(Block block);
        R visitExpressionStmt(Expression expression);
        R visitIfConditionStmt(IfCondition ifcondition);
        R visitPrintStmt(Print print);
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
