package lox.ast;

import lox.scanner.Token;

import java.util.List;

public abstract class Expr {

    public interface Visitor<R> {
        R visitAssignExpr(Assign assign);
        R visitBinaryExpr(Binary binary);
        R visitCallExpr(Call call);
        R visitGetExpr(Get get);
        R visitSetExpr(Set set);
        R visitLoxSuperExpr(LoxSuper loxsuper);
        R visitLoxThisExpr(LoxThis loxthis);
        R visitGroupingExpr(Grouping grouping);
        R visitLiteralExpr(Literal literal);
        R visitLogicalExpr(Logical logical);
        R visitUnaryExpr(Unary unary);
        R visitTernaryExpr(Ternary ternary);
        R visitVariableExpr(Variable variable);
}

    public static class Assign extends Expr {

        public final Token name;
        public final Expr value;
        public Assign(Token name, Expr value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }
    }

    public static class Binary extends Expr {

        public final Expr left;
        public final Token operator;
        public final Expr right;
        public Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    public static class Call extends Expr {

        public final Expr callee;
        public final Token paren;
        public final List<Expr> arguments;
        public Call(Expr callee, Token paren, List<Expr> arguments) {
            this.callee = callee;
            this.paren = paren;
            this.arguments = arguments;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }
    }

    public static class Get extends Expr {

        public final Expr object;
        public final Token name;
        public Get(Expr object, Token name) {
            this.object = object;
            this.name = name;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetExpr(this);
        }
    }

    public static class Set extends Expr {

        public final Expr object;
        public final Token name;
        public final Expr value;
        public Set(Expr object, Token name, Expr value) {
            this.object = object;
            this.name = name;
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitSetExpr(this);
        }
    }

    public static class LoxSuper extends Expr {

        public final Token keyword;
        public final Token method;
        public LoxSuper(Token keyword, Token method) {
            this.keyword = keyword;
            this.method = method;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLoxSuperExpr(this);
        }
    }

    public static class LoxThis extends Expr {

        public final Token keyword;
        public LoxThis(Token keyword) {
            this.keyword = keyword;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLoxThisExpr(this);
        }
    }

    public static class Grouping extends Expr {

        public final Expr expression;
        public Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
    }

    public static class Literal extends Expr {

        public final Object value;
        public Literal(Object value) {
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }

    public static class Logical extends Expr {

        public final Expr left;
        public final Token operator;
        public final Expr right;
        public Logical(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
        }
    }

    public static class Unary extends Expr {

        public final Token operator;
        public final Expr right;
        public Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    public static class Ternary extends Expr {

        public final Expr condition;
        public final Expr trueCondition;
        public final Expr falseCondition;
        public Ternary(Expr condition, Expr trueCondition, Expr falseCondition) {
            this.condition = condition;
            this.trueCondition = trueCondition;
            this.falseCondition = falseCondition;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitTernaryExpr(this);
        }
    }

    public static class Variable extends Expr {

        public final Token name;
        public Variable(Token name) {
            this.name = name;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }
    }


    public abstract <R> R accept(Visitor<R> visitor);
}
