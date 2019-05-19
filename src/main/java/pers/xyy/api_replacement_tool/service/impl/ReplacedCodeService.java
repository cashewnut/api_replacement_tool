package pers.xyy.api_replacement_tool.service.impl;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.xyy.api_replacement_tool.model.DeprecatedAPI;
import pers.xyy.api_replacement_tool.model.Line;
import pers.xyy.api_replacement_tool.model.ReplacedCode;
import pers.xyy.api_replacement_tool.service.IDeprecatedAPIService;
import pers.xyy.api_replacement_tool.service.IReplacedCodeService;
import pers.xyy.api_replacement_tool.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReplacedCodeService implements IReplacedCodeService {

    @Autowired
    private IDeprecatedAPIService deprecatedAPIService;


    private void initDeprecatedAPI(ReplacedCode replacedCode) {
        CompilationUnit cu = replacedCode.getCu();
        List<MethodCallExpr> deprecatedAPIs = new ArrayList<>();
        List<MethodCallExpr> mcs = cu.findAll(MethodCallExpr.class);
        for (MethodCallExpr mc : mcs) {
            DeprecatedAPI api = deprecatedAPIService.getByMethodCallExpr(mc);
            if (api != null)
                deprecatedAPIs.add(mc);
        }
        replacedCode.setMethodCallExprs(deprecatedAPIs);
    }

    @Override
    public void initLines(ReplacedCode replacedCode) {
        List<Line> lines = new ArrayList<>();
        CompilationUnit cu = replacedCode.getCu();
        for (String content : cu.toString().split("\\n")) {
            Line line = new Line();
            line.setContent(content);
            lines.add(line);
        }
        replacedCode.setLines(lines);
    }

    @Override
    public void updateLine(ReplacedCode replacedCode) {
        List<MethodCallExpr> methodCallExprs = replacedCode.getMethodCallExprs();
        List<Line> lines = replacedCode.getLines();
        CompilationUnit cu = replacedCode.getCu();
        for (MethodCallExpr mc : methodCallExprs) {
            Line line = lines.get(mc.getBegin().get().line - 1);
            String code = FileUtil.readLine(mc.getBegin().get().line, cu.toString());
            String method = mc.toString();
            int start = code.indexOf(method);
            int scopeLength = mc.getScope().isPresent() ? mc.getScope().get().toString().length() + 1 : 0;
            start += scopeLength;
            method = method.substring(scopeLength);
            int end = start + method.length();
            line.setIndex(start, end);
        }
    }

    @Override
    public ReplacedCode getReplaceCode(String code) {
        CompilationUnit cu = JavaParser.parse(code);
        ReplacedCode replacedCode = new ReplacedCode(cu);
        initDeprecatedAPI(replacedCode);
        initLines(replacedCode);
        updateLine(replacedCode);
        return replacedCode;
    }


    public static void main(String[] args) {
        CompilationUnit cu = FileUtil.openCU("/Users/xiyaoguo/Documents/workspace/IntelliJ IDEA/api_replacement_tool/src/main/resources/examples/ExampleA");
//        MethodCallExpr mc = cu.findAll(MethodCallExpr.class).get(0);
//        String code = FileUtil.readLine(mc.getBegin().get().line, cu.toString());
//        String method = mc.toString();
//        int start = code.indexOf(method);
//        int scopeLength = mc.getScope().isPresent() ? mc.getScope().get().toString().length() + 1 : 0;
//        start += scopeLength;
//        method = method.substring(scopeLength);
//        int end = start + method.length();
//        System.out.println(code);
//        System.out.println("start:" + start + " end:" + end);

    }

}
