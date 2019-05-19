package pers.xyy.api_replacement_tool.model;

public class DeprecatedAPI {
    private Integer id;
    private String packageName;
    private String className;
    private String methodName;
    private String methodReturnType;
    private String methodArgs;
    private String rPackageName;
    private String rClassName;
    private String rMethodName;
    private String rReturnType;
    private String rMethodArgs;
    private String rInvoker;
    private Integer type;
    private String replace;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodReturnType() {
        return methodReturnType;
    }

    public void setMethodReturnType(String methodReturnType) {
        this.methodReturnType = methodReturnType;
    }

    public String getMethodArgs() {
        return methodArgs;
    }

    public void setMethodArgs(String methodArgs) {
        this.methodArgs = methodArgs;
    }

    public String getrPackageName() {
        return rPackageName;
    }

    public void setrPackageName(String rPackageName) {
        this.rPackageName = rPackageName;
    }

    public String getrClassName() {
        return rClassName;
    }

    public void setrClassName(String rClassName) {
        this.rClassName = rClassName;
    }

    public String getrMethodName() {
        return rMethodName;
    }

    public void setrMethodName(String rMethodName) {
        this.rMethodName = rMethodName;
    }

    public String getrReturnType() {
        return rReturnType;
    }

    public void setrReturnType(String rReturnType) {
        this.rReturnType = rReturnType;
    }

    public String getrMethodArgs() {
        return rMethodArgs;
    }

    public void setrMethodArgs(String rMethodArgs) {
        this.rMethodArgs = rMethodArgs;
    }

    public String getrInvoker() {
        return rInvoker;
    }

    public void setrInvoker(String rInvoker) {
        this.rInvoker = rInvoker;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }


}
