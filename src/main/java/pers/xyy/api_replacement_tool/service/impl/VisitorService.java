package pers.xyy.api_replacement_tool.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.xyy.api_replacement_tool.model.*;
import pers.xyy.api_replacement_tool.service.IDeprecatedAPIService;
import pers.xyy.api_replacement_tool.service.IVisitorService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VisitorService implements IVisitorService {

    @Autowired
    private IDeprecatedAPIService service;

    @Override
    public void replace(ReplacedCode replacedCode, int index) {
        MethodCallExpr mc = replacedCode.getMethodCallExprs().get(index);
        replace(replacedCode, mc);
    }

    @Override
    public void replaceAll(ReplacedCode replacedCode) {
        for (MethodCallExpr mc : replacedCode.getMethodCallExprs())
            replace(replacedCode, mc);
    }

    private void replace(ReplacedCode replacedCode, MethodCallExpr mc) {
        DeprecatedAPI api = service.getByMethodCallExpr(mc);
        if (api == null)
            return;
        Replace replace = JSON.parseObject(api.getReplace(), Replace.class);

        switch (api.getType()) {
            case 1:
                replaceType1(mc, replace);
                break;
            case 2:
                replaceType2(mc, replace);
                break;
            case 3:
                replaceType3(mc, replace);
                break;
            case 4:
                replaceType4(mc, replace);
                break;
            default:
                break;
        }

        addImports(replacedCode, api);//add imports
        if (replace.getComments() != null) {
            Node node = findStatement(mc);
            node.setComment(new LineComment(String.join("\n", replace.getComments())));
        }
    }

    /**
     * 直接替换，只有方法名改变
     *
     * @param mc
     * @param replace
     */
    private void replaceType1(MethodCallExpr mc, Replace replace) {
        if (replace == null)
            return;
        Method methodDesc = replace.getMethods().get(0);
        //name
        mc.setName(methodDesc.getName());
    }

    /**
     * invoker相同或者存在父子类关系，参数需要调整
     *
     * @param mc
     * @param replace
     */
    private void replaceType2(MethodCallExpr mc, Replace replace) {
        if (replace == null)
            return;
        Method methodDesc = replace.getMethods().get(0);
        //TODO type2情况，感觉和type3的处理没什么差别
        //scope不变所以不需要改动

        //name
        mc.setName(methodDesc.getName());
        //args
        if (methodDesc.getArgs() != null)
            mc.setArguments(generateNewArgs(methodDesc.getArgs(), mc));
    }

    /**
     * static方法或者invoker不同
     *
     * @param mc
     * @param replace
     */
    private void replaceType3(MethodCallExpr mc, Replace replace) {
        if (replace == null)
            return;
        Method methodDesc = replace.getMethods().get(0);

        //scope
        if (methodDesc.getInvoker() != null) {
            if (methodDesc.getRelated() == null || !methodDesc.getRelated()) {
                if (methodDesc.getInvoker().isEmpty())
                    mc = mc.removeScope();
                else
                    mc.setScope(new NameExpr(placeArgsHolder(methodDesc.getInvoker(), mc)));
            } else
                mc.setScope(new NameExpr(placeArgsHolder(methodDesc.getInvoker(), mc)));
        }
        //name
        mc.setName(methodDesc.getName());
        //args
        if (methodDesc.getArgs() != null)
            mc.setArguments(generateNewArgs(methodDesc.getArgs(), mc));
    }

    /**
     * 多个方法组合
     *
     * @param mc
     * @param replace
     */
    private void replaceType4(MethodCallExpr mc, Replace replace) {
        if (replace == null)
            return;
        List<Method> methodDescs = replace.getMethods();
        List<String> methods = new ArrayList<>();
        for (Method methodDesc : methodDescs)
            methods.add(transMethod(methodDesc, mc));
        mc.replace(new NameExpr(placeMethodsHolder(mc, replace.getOperations().get(0), methods)));
    }

    //把method转成String,和replaceType3有很多重复代码，可以考虑重构
    private String transMethod(Method method, MethodCallExpr rowMc) {
        MethodCallExpr mc = new MethodCallExpr();
        //scope
        if (method.getInvoker() != null) {
            if (method.getRelated() == null || !method.getRelated())
                mc.setScope(new NameExpr(method.getInvoker()));
            else
                mc.setScope(new NameExpr(placeArgsHolder(method.getInvoker(), mc)));
        }
        //name
        mc.setName(method.getName());
        //args
        if (method.getArgs() != null)
            mc.setArguments(generateNewArgs(method.getArgs(), rowMc));

        return mc.toString();
    }

    //将oldArgs转为newArgs
    private NodeList<Expression> generateNewArgs(List<Args> argsDesc, MethodCallExpr mc) {
        NodeList<Expression> oldArgs = mc.getArguments();
        NodeList<Expression> newArgs = new NodeList<>();
        if (argsDesc == null)
            return newArgs;
        for (int i = 0; i < argsDesc.size(); i++) {
            Args arg = argsDesc.get(i);
            if (arg.getRelated() == null || !arg.getRelated())
                newArgs.add(new NameExpr(arg.getName()));
            else if (arg.getName().contains("$invoker"))
                newArgs.add(new NameExpr(arg.getName().replace("$invoker", mc.getScope().get().toString())));
            else if (arg.getOperations() == null || arg.getOperations().isEmpty()) {
                int index = Integer.parseInt(arg.getName().replace("$dArgs", ""));
                newArgs.add(oldArgs.get(index));
            } else {
                List<String> operations = arg.getOperations();
                for (int j = 0; j < operations.size(); j++)
                    operations.set(j, placeArgsHolder(operations.get(j), mc));
                if (operations.size() == 1 && operations.get(0).startsWith("$this")) {
                    newArgs.add(new NameExpr(placeArgsHolder(operations.get(0).replace("$this = ", ""), mc)));
                } else {
                    /*
                      TODO 如果不是1的情况如何考虑
                      找到上层的BlockStmt，添加进去

                      先解决BlockStmt的情况，之后有时间再改
                     */
                    String argName = "_args_" + i;
                    String argType = arg.getType();
                    List<Statement> statements = argsOperationsStatement(operations, argName, argType);

                    try {
                        Statement curStatement = (Statement) findStatement(mc);
                        if (curStatement.getParentNode().isPresent()) {
                            BlockStmt blockStmt = (BlockStmt) curStatement.getParentNode().get();
                            int index = blockStmt.getChildNodes().indexOf(curStatement);
                            for (int j = statements.size() - 1; j >= 0; j--)
                                blockStmt.addStatement(index, statements.get(j));
                        }
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                    newArgs.add(new NameExpr(argName));
                }
            }
        }
        return newArgs;
    }

    private List<Statement> argsOperationsStatement(List<String> operations, String argName, String argType) {
        boolean firstThis = true;
        List<Statement> ret = new ArrayList<>();
        for (int j = 0; j < operations.size(); j++) {
            String operation = operations.get(j);
            if (operation.contains("$this")) {
                if (firstThis && operation.startsWith("$this")) {
                    operations.set(j, operation.replace("$this", argType + " " + argName));
                    firstThis = false;
                } else
                    operations.set(j, operation.replace("$this", argName));
            }
            ret.add(new ExpressionStmt(new NameExpr(operations.get(j))));
        }
        return ret;
    }

    //找到mc所在的statement
    private Node findStatement(MethodCallExpr mc) {
        Node statement = mc;
        while (statement.getParentNode().isPresent()) {
            if (statement.getParentNode().get() instanceof BlockStmt)
                return statement;
            else
                statement = statement.getParentNode().get();
        }
        return statement;
    }

    //更换占位符，$m0,$m1...
    private String placeMethodsHolder(MethodCallExpr mc, String origin, List<String> methods) {
        for (int i = 0; i < methods.size(); i++)
            origin = origin.replace("$m" + i, methods.get(i));
        if (origin.contains("$invoker"))
            origin = origin.replace("$invoker", mc.getScope().get().toString());
        return origin;
    }


    //更换占位符，$dArgs1...
    private String placeArgsHolder(String origin, MethodCallExpr mc) {
        NodeList<Expression> oldArgs = mc.getArguments();
        while (origin.contains("$dArgs")) {
            int i = origin.indexOf("$dArgs");
            String str1 = origin.substring(0, i);
            int index = 0;
            i += 6;
            while (i < origin.length() && Character.isDigit(origin.charAt(i))) {
                index = index * 10 + (origin.charAt(i) - '0');
                i++;
            }
            String str2 = origin.substring(i);
            origin = str1 + oldArgs.get(index) + str2;
        }
        if (origin.contains("$invoker"))
            origin = origin.replace("$invoker", mc.getScope().get().toString());
        return origin;
    }

    private void addImports(ReplacedCode replacedCode, DeprecatedAPI api) {
        Set<String> imports = getImports(replacedCode);
        String packageName = api.getrPackageName() + "." + api.getrClassName();
        if (!imports.contains(packageName))
            replacedCode.getCu().addImport(packageName);
    }

    private Set<String> getImports(ReplacedCode replacedCode) {
        Set<String> set = new HashSet<>();
        CompilationUnit cu = replacedCode.getCu();
        if (cu.getImports() != null) {
            for (ImportDeclaration importDeclaration : cu.getImports()) {
                set.add(importDeclaration.getNameAsString());
            }
        }
        return set;
    }
}
