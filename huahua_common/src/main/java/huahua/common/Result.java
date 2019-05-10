package huahua.common;

//公共的返回类

import lombok.Data;

@Data
public class Result {

private  boolean flag;//是否成功
private Integer code;//状态吗
private String message;//请求信息
private Object data;//请求数据（相应数据）

    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }


    public Result(boolean flag, Integer code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result() {

    }

    public boolean isFlag() {
        return flag;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

}
