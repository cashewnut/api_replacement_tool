package pers.xyy.api_replacement_tool.model;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.List;

public class ReplacedCode {

    private CompilationUnit cu;

    private List<Line> lines;

    private List<MethodCallExpr> methodCallExprs;


    public ReplacedCode() {
    }

    public ReplacedCode(CompilationUnit cu) {
        this.cu = cu;
    }

    public void setCu(CompilationUnit cu) {
        this.cu = cu;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public void setMethodCallExprs(List<MethodCallExpr> methodCallExprs) {
        this.methodCallExprs = methodCallExprs;
    }

    public CompilationUnit getCu() {
        return cu;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<MethodCallExpr> getMethodCallExprs() {
        return methodCallExprs;
    }

}
