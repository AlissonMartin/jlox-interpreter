# jlox-interpreter 🚀

This project is a complete implementation of the Lox language interpreter, based on the book "Crafting Interpreters" by Robert Nystrom. The interpreter was developed in Java and follows the entire architecture proposed in the book, including lexical analysis, parsing, variable resolution, and code execution.

## 📁 Project Structure

- `lox/` — Java source files for the interpreter.
- `tool/` — Auxiliary tools, such as the AST generator (`GenerateAst.java`).
- `lox/test.lox` — Example Lox code for testing.

## ✅ Prerequisites

- Java JDK 8 or higher installed.

## 🛠️ How to Compile

1. Navigate to the project root:
   ```sh
   cd C:/Users/aliss/OneDrive/Documentos/Projetos/jlox-interpreter
   ```
2. Compile all Java files:
   ```sh
   javac lox/*.java tool/*.java
   ```

## ▶️ How to Run

1. Run the interpreter by passing a `.lox` file as an argument:
   ```sh
   java lox.Lox lox/test.lox
   ```
   Or run without arguments to use the REPL mode (interactive command line):
   ```sh
   java lox.Lox
   ```

2. To test other files, just create a new `.lox` file and pass the path as an argument.

## 🧰 Generating the AST

If you modify the expression or statement classes, regenerate the AST classes:
```sh
java tool.GenerateAst lox
```

## 💡 Example Lox Code

```lox
print "Hello, world!";

var a = 10;
var b = 20;
print a + b;
```

## 📝 Implemented Features

- [x] Lexical analysis (Scanner)
- [x] Syntactic analysis (Parser)
- [x] Variable resolution (Resolver)
- [x] Code execution (Interpreter)
- [x] Support for functions, classes, inheritance, and lexical scope
- [x] Interactive REPL mode

## 📚 References
- Book: [Crafting Interpreters](https://craftinginterpreters.com/)
- Author: Robert Nystrom

---