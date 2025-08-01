package tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
         if (args.length != 1) {
             System.err.println("Usage: generate_ast <output directory>");
             System.exit(64);
         }
         String outputDir = args[0];
         defineAst(outputDir, "Expr", Arrays.asList("Assign : Token name, Expr value", "Binary : Expr left, Token operator, Expr right", "Call : Expr callee, Token paren, List<Expr> arguments", "Get : Expr object, Token name", "Set : Expr object, Token name, Expr value", "LoxSuper : Token keyword, Token method", "LoxThis : Token keyword", "Grouping : Expr expression", "Literal : Object value", "Logical : Expr left, Token operator, Expr right", "Unary : Token operator, Expr right", "Ternary : Expr condition, Expr trueCondition, Expr falseCondition", "Variable : Token name"));

         defineAst(outputDir, "Stmt", Arrays.asList("Block : List<Stmt> statements", "ClassDef : Token name, Expr.Variable superclass, List<Stmt.Function> methods, List<Stmt.Function> staticMethods", "Expression : Expr expression", "Function : Token name, List<Token> params, List<Stmt> body", "IfCondition : Expr condition, Stmt thenBranch," + " Stmt elseBranch", "Print : Expr expression","ReturnStmt : Token keyword, Expr value", "Var : Token name, Expr initializer", "WhileLoop : Expr condition, Stmt body"));
    }


    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";

        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.println("package lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        writer.println();

        defineVisitor(writer, baseName, types);

        writer.println();

        // AST classes

        for (String type : types) {
            String className = type.split(":")[0].trim();

            String fields = type.split(":")[1].trim();

            defineType(writer, baseName, className, fields);
        }

        writer.println();

        writer.println("    abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");

        writer.close();

    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("    interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();

            writer.println("        R visit" + typeName + baseName + "(" + typeName + " " + typeName.toLowerCase() + ");");
        }

        writer.println("}");
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        String[] fields = fieldList.split(", ");

        writer.println("    static class " + className + " extends " + baseName + " {");
        writer.println();

        for (String field : fields) {
            writer.println("        final " + field + ";");
        }

        writer.println("        " + className + "(" + fieldList + ") {");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("            this." + name + " = " + name + ";");
        }
        writer.println("        }");

        writer.println();

        writer.println("        @Override");
        writer.println("        <R> R accept(Visitor<R> visitor) {");
        writer.println("            return visitor.visit" + className + baseName + "(this);");
        writer.println("        }");

        writer.println("    }");
        writer.println();
    }


}
