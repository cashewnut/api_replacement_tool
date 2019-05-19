package pers.xyy.api_replacement_tool.service;

import pers.xyy.api_replacement_tool.model.ReplacedCode;

public interface IReplacedCodeService {

    public ReplacedCode getReplaceCode(String code);

    public void initLines(ReplacedCode replacedCode);

    public void updateLine(ReplacedCode replacedCode);

}
