package pers.xyy.api_replacement_tool.service;

import pers.xyy.api_replacement_tool.model.ReplacedCode;

public interface IVisitorService {

    public void replace(ReplacedCode replacedCode, int index);

}
