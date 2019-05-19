package pers.xyy.api_replacement_tool.service;

import pers.xyy.api_replacement_tool.resource.ReplaceResource;
import pers.xyy.api_replacement_tool.resource.Response;

public interface IReplaceService {

    public String getExamples();

    public Response analyze(String code);

    public Response replace(ReplaceResource replaceResource);

    public Response revert();

}
