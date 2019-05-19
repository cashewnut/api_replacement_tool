package pers.xyy.api_replacement_tool;

public enum ResponseCode {
    SUCCESS(600),
    SUCCESS_CANNOT_REVERT(601),
    IMPORTS_INCOMPLETE(700),
    CODE_SYNTAX(701),
    UNKNOWN_EXCEPTION(702);

    private int value;

    ResponseCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
