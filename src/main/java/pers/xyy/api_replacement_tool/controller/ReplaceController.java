package pers.xyy.api_replacement_tool.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.xyy.api_replacement_tool.resource.ReplaceResource;
import pers.xyy.api_replacement_tool.service.IReplaceService;

@RestController
@RequestMapping("/replace")
public class ReplaceController {

    @Autowired
    private IReplaceService service;

    @GetMapping(value = "/examples")
    @CrossOrigin
    public String getExamples() {
        System.out.println(JSON.toJSONString(service.getExamples()));
        return service.getExamples();
    }

    @ResponseBody
    @PostMapping(value = "/analyze")
    @CrossOrigin
    public String analyze(@RequestBody String code) {
        JSONObject jsonObject = JSON.parseObject(code);
        code = (String) jsonObject.get("code");
        return JSON.toJSONString(service.analyze(code));
    }

    @GetMapping(value = "/replace/{index}")
    @CrossOrigin
    public String replace(@PathVariable("index") int index) {
        return JSON.toJSONString(service.replace(index));
    }

    @GetMapping(value = "/revert")
    @CrossOrigin
    public String revert() {
        return JSON.toJSONString(service.revert());
    }

    @GetMapping(value = "/replaceall")
    @CrossOrigin
    public String replaceAll() {
        return JSON.toJSONString(service.replaceAll());
    }

}
