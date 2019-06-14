package pers.xyy.api_replacement_tool.service.impl;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.xyy.api_replacement_tool.exceptions.ImportsIncompleteException;
import pers.xyy.api_replacement_tool.model.DeprecatedAPI;
import pers.xyy.api_replacement_tool.persistence.IDeprecatedAPIDAO;
import pers.xyy.api_replacement_tool.service.IDeprecatedAPIService;

import java.util.List;

@Service
public class DeprecatedAPIService implements IDeprecatedAPIService {

    static {
        TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver());
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
    }

    @Autowired
    private IDeprecatedAPIDAO deprecatedAPIDAO;

    @Override
    public DeprecatedAPI getById(Integer id) {
        return deprecatedAPIDAO.getById(id);
    }

    @Override
    public List<DeprecatedAPI> getDeprecatedAPI() {
        return deprecatedAPIDAO.getDeprecatedAPI();
    }

    @Override
    public DeprecatedAPI getByDeprecated(DeprecatedAPI api) {
        DeprecatedAPI deprecatedAPI = deprecatedAPIDAO.getByDeprecated(api);
        if (deprecatedAPI == null && api.getMethodReturnType().contains(".")) {
            String[] types = api.getMethodReturnType().split("[.]");
            api.setMethodReturnType(types[types.length - 1]);
            deprecatedAPI = deprecatedAPIDAO.getByDeprecated(api);
        }
        return deprecatedAPI;
    }

    @Override
    public DeprecatedAPI getByMethodCallExpr(MethodCallExpr mc) {
        DeprecatedAPI api = new DeprecatedAPI();
        System.out.println(mc.getName());
        try {
            if (mc.getNameAsString().equals("engineCanResolve")) {
                api = getById(129);
                return api;
            }
            if (mc.getNameAsString().equals("engineResolve")) {
                return getById(128);
            }
            if (mc.getNameAsString().equals("setValue") && mc.getScope().isPresent() && mc.getScope().get().toString().equals("attr"))
                return null;
            ResolvedMethodDeclaration rmd = mc.resolveInvokedMethod();
            api.setPackageName(rmd.getPackageName());
            api.setClassName(rmd.getClassName());
            api.setMethodReturnType(rmd.getReturnType().describe());
            api.setMethodName(rmd.getName());
            StringBuilder rArgs = new StringBuilder();
            for (int i = 0; i < rmd.getNumberOfParams(); i++)
                rArgs.append(rmd.getParam(i).describeType()).append(",");
            if (rArgs.toString().endsWith(","))
                rArgs = new StringBuilder(rArgs.substring(0, rArgs.length() - 1));
            api.setMethodArgs(rArgs.toString());
        } catch (RuntimeException e) {
            if (mc.getNameAsString().equals("create") && mc.getScope().isPresent() && mc.getScope().get().toString().equals("factory"))
                return getById(50);
            throw new ImportsIncompleteException("check imports!");
        }

        api = getByDeprecated(api);
        return api;
    }
}
