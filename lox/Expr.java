package lox;

import java.util.List;

abstract class Expr {

    interface Visitor<R> {
        R visitAssignExpr(Assign assign);
        R visitBinaryExpr(Binary binary);
        R visitGroupingExpr(Grouping grouping);
        R visitLiteralExpr(Literal literal);
        R visitLogicalExpr(Logical logical);
        R visitUnaryExpr(Unary unary);
        R visitTernaryExpr(Ternary ternary);
        R visitVariableExpr(Variable variable);
}

    static class Assign extends Expr {

        final Token name;
        final Expr value;
        Assign(Token name, Expr value) {
            this.name = name;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }
    }

    static class Binary extends Expr {

        final Expr left;
        final Token operator;
        final Expr right;
        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    static class Grouping extends Expr {

        final Expr expression;
        Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
    }

    static class Literal extends Expr {

        final Object value;
        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }

    static class Logical extends Expr {

        final Expr left;
        final Token operator;
        final Expr right;
        Logical(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
        }
    }

    static class Unary extends Expr {

        final Token operator;
        final Expr right;
        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    static class Ternary extends Expr {

        final Expr condition;
        final Expr trueCondition;
        final Expr falseCondition;
        Ternary(Expr condition, Expr trueCondition, Expr falseCondition) {
            this.condition = condition;
            this.trueCondition = trueCondition;
            this.falseCondition = falseCondition;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitTernaryExpr(this);
        }
    }

    static class Variable extends Expr {

        final Token name;
        Variable(Token name) {
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }
    }


    abstract <R> R accept(Visitor<R> visitor);
}
