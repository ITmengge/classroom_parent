package com.simon.classroom.exception;

import com.simon.classroom.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类：使用 aop 的@ControllerAdvice；在异常处理方法上加@ExceptionHandler 和 @ResponseBody
 */
@ControllerAdvice   // aop 面向切面编程
public class GlobalExceptionHandler {

    /**
     * 1、全局异常处理（最后执行的）
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        System.out.println("全局异常");
        e.printStackTrace();
        return Result.fail(null).message("执行了全局异常处理");
    }

    /**
     * 2、特定异常处理，如：java.lang.ArithmeticException: / by zero
     */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        System.out.println("特定异常");
        e.printStackTrace();
        return Result.fail(null).message("执行了ArithmeticException异常处理");
    }

    /**
     * 3、自定义异常处理：继承RuntimeException、创建属性、在全局异常处理类添加自定义异常处理的方法、手动抛出自定义异常
     */
    @ExceptionHandler(ClassroomException.class)
    @ResponseBody
    public Result error(ClassroomException e){
        System.out.println("自定义异常");
        e.printStackTrace();
        return Result.fail(null).code(e.getCode()).message(e.getMessage());
    }

}
