package lox;

import lox.ast.Stmt;
import lox.interpreter.Interpreter;
import lox.parser.Parser;
import lox.resolver.Resolver;
import lox.scanner.Scanner;
import lox.scanner.Token;
import lox.util.RuntimeError;
import lox.util.TokenType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    private static final Interpreter interpreter = new Interpreter();

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.exit(64);
        }
        else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String filepath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filepath));

        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(isr);

        for(;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            if (hadError) System.exit(65);
        }
    }

    private static void run(String source)  {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        if (hadError) return;

        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(statements);

        if (hadError) return; // Checks for resolver errors

        if (statements.size() == 1 && statements.get(0) instanceof Stmt.Expression) {
            Object value = interpreter.evaluate(((Stmt.Expression) statements.get(0)).expression);

            if (value != null) {
                System.out.println(interpreter.stringify(value));
            }
        }

        interpreter.interpret(statements);
    }

    // Error
    public static void error(int line, String message) {
        report(line, "", message);
    }

    public static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, "at end", message);
        } else {
            report(token.line, "at '" + token.lexeme + "'", message);
        }
    }

    public static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }

    private static void report(int line, String where, String message) {
        System.out.println("[line " + line + "] " + where + ": " + message);
        hadError = true;
    }
}
