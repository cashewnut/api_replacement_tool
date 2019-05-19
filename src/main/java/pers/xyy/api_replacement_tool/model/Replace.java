package pers.xyy.api_replacement_tool.model;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class Replace {

    private List<Method> methods;
    private List<String> operations;

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public static void main(String[] args) {
//        Replace replace = new Replace();
//        Method method = new Method();
//        method.setName("getComponent");
//        Args args1 = new Args();
//        args1.setName("i");
//        args1.setType("int");
//        args1.setRelated(true);
//        args1.setOperations(Collections.singletonList("$this = $dArgs1"));
//        method.setArgs(Collections.singletonList(args1));
//        method.setInvoker("");
//        replace.setMethods(Collections.singletonList(method));
//        replace.setOperations(Collections.singletonList("$m1"));
//        System.out.println(JSON.toJSONString(replace));
        Replace replace = JSON.parseObject("{\"methods\":[{\"args\":[{\"name\":\"i\",\"operations\":[\"$this = $dArgs1\"],\"related\":true,\"type\":\"int\"}],\"invoker\":\"\",\"name\":\"getComponent\"}],\"operations\":[\"$m1\"]}",Replace.class);
        System.out.println(replace);
    }
}
