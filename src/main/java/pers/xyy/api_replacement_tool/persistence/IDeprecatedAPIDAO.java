package pers.xyy.api_replacement_tool.persistence;

import pers.xyy.api_replacement_tool.model.DeprecatedAPI;

import java.util.List;

public interface IDeprecatedAPIDAO {

    public DeprecatedAPI getById(Integer id);

    public List<DeprecatedAPI> getDeprecatedAPI();

    public DeprecatedAPI getByDeprecated(DeprecatedAPI api);

}
