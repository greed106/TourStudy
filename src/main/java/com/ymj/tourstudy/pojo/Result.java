package com.ymj.tourstudy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    //响应码
    private Integer code;
    //响应信息，描述字符串
    private String msg;
    //返回的数据
    private Object data;
    //增删改的成功响应
    public static Result success(){
        return new Result(1,"success",null);
    }
    //查询的成功响应
    public static Result success(Object data){
        return new Result(200,"success",data);
    }
    //失败相应
    public static Result error(String msg){
        return new Result(400,msg,null);
    }
}