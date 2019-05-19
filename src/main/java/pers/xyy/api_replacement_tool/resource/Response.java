package pers.xyy.api_replacement_tool.resource;

import pers.xyy.api_replacement_tool.ResponseCode;
import pers.xyy.api_replacement_tool.model.Line;

import java.util.List;

public class Response {

    private Integer responseCode;

    private String message;

    private List<Line> lines;

    public Response() {

    }

    public Response(List<Line> lines) {
        this.lines = lines;
        this.responseCode = ResponseCode.SUCCESS.getValue();
        this.message = ResponseCode.SUCCESS.name();
    }

    public Response(Integer responseCode, String message, List<Line> lines) {
        this.responseCode = responseCode;
        this.message = message;
        this.lines = lines;
    }

    public Response(Integer responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

}
