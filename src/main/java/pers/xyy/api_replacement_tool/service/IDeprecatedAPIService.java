package pers.xyy.api_replacement_tool.service;

import com.github.javaparser.ast.expr.MethodCallExpr;
import pers.xyy.api_replacement_tool.model.DeprecatedAPI;

import java.util.List;

public interface IDeprecatedAPIService {

    public DeprecatedAPI getById(Integer id);

    public List<DeprecatedAPI> getDeprecatedAPI();

    public DeprecatedAPI getByDeprecated(DeprecatedAPI api);

    public DeprecatedAPI getByMethodCallExpr(MethodCallExpr mc);

}
