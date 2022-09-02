package com.simon.classroom.result;

import lombok.Data;

// 统一返回结果类
@Data
public class Result<T> {

    private Integer code;       // 状态码

    private String message;     // 返回状态信息（成功/失败）

    private T data;             // 返回数据

    public Result(){}

//    /**
//     * 成功的方法，没有data数据
//     * @param <T>
//     * @return
//     */
//    public static<T> Result<T> success(){
//        Result<T> result = new Result<>();
//        result.setCode(200);
//        result.setMessage("成功");
//        return result;
//    }

//    /**
//     * 失败的方法，没有data数据
//     * @param <T>
//     * @return
//     */
//    public static<T> Result<T> fail(){
//        Result<T> result = new Result<>();
//        result.setCode(201);
//        result.setMessage("失败");
//        return result;
//    }

    /**
     * 成功的方法，没有data数据
     * @param <T>
     * @return
     */
    public static<T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.setCode(20000);
        result.setMessage("成功");
        if (data != null){
            result.setData(data);
        }
        return result;
    }

    /**
     * 失败的方法，没有data数据
     * @param <T>
     * @return
     */
    public static<T> Result<T> fail(T data){
        Result<T> result = new Result<>();
        result.setCode(20001);
        result.setMessage("失败");
        if (data != null){
            result.setData(data);
        }
        return result;
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }
}
