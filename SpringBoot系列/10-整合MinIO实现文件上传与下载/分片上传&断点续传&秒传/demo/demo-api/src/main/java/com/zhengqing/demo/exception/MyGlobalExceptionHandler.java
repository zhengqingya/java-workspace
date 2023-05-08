package com.zhengqing.demo.exception;

import com.zhengqing.demo.model.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * <p>
 * 全局异常处理器
 * </p>
 *
 * @author zhengqingya
 * @description 在spring 3.2中，新增了@ControllerAdvice
 * 注解，可以用于定义@ExceptionHandler、@InitBinder、@ModelAttribute，并应用到所有@RequestMapping中
 * @date 2019/8/25 0025 18:56
 */
@Slf4j
@RestControllerAdvice
public class MyGlobalExceptionHandler {

    /**
     * 自定义异常处理
     */
    @ExceptionHandler(value = MyException.class)
    public ApiResult myException(MyException e) {
        log.error("自定义异常：", e);
        if (e.getCode() != null) {
            return ApiResult.fail(e.getCode(), e.getMessage());
        }
        return ApiResult.fail(e.getMessage());
    }


    // 参数校验异常处理 ===========================================================================
    // MethodArgumentNotValidException是springBoot中进行绑定参数校验时的异常,需要在springBoot中处理,其他需要处理ConstraintViolationException异常进行处理.

    /**
     * 方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("方法参数校验:" + e.getMessage(), e);
        return ApiResult.fail(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public ApiResult handleValidationException(ValidationException e) {
        log.error("ValidationException:", e);
        Throwable cause = e.getCause();
        if (cause == null) {
            return ApiResult.fail(e.getMessage());
        }
        String message = cause.getMessage();
        return ApiResult.fail(message);
    }

    /**
     * jsr303参数校验异常
     */
    @ExceptionHandler({BindException.class})
    public ApiResult<String> exception(BindException e) {
        log.error("BindException:", e);
        return ApiResult.fail(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult handleConstraintViolationException(ConstraintViolationException e) {
        log.error("ValidationException:" + e.getMessage(), e);
        return ApiResult.fail(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResult<String> handleRuntimeException(IllegalArgumentException e) {
        log.error("参数不合法:", e);
        return ApiResult.fail(e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResult handlerNoFoundException(Exception e) {
        log.error("404:", e);
        return ApiResult.fail(404, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ApiResult handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("数据重复，请检查后提交:", e);
        return ApiResult.fail("数据重复，请检查后提交:" + e.getMessage());
    }

    // ===============================================

    @ExceptionHandler(RuntimeException.class)
    public ApiResult handleRuntimeException(RuntimeException e) {
        log.error("系统异常:", e);
        return ApiResult.fail("系统异常，操作失败:" + e.getMessage());
    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ApiResult nullPointerExceptionHandler(NullPointerException e) {
        log.error("空指针异常:", e);
        return ApiResult.fail("空指针异常:" + e.getMessage());
    }

    /**
     * 类型转换异常
     */
    @ExceptionHandler(ClassCastException.class)
    public ApiResult classCastExceptionHandler(ClassCastException e) {
        log.error("类型转换异常:", e);
        return ApiResult.fail("类型转换异常:" + e.getMessage());
    }

    /**
     * 数组越界异常
     */
    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ApiResult arrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e) {
        log.error("数组越界异常:", e);
        return ApiResult.fail("数组越界异常:" + e.getMessage());
    }

    /**
     * 包含调用处理程序抛出的未声明的检查异常
     */
    @ExceptionHandler({UndeclaredThrowableException.class})
    public ApiResult exception(UndeclaredThrowableException e) {
        log.error("UndeclaredThrowableException:", e);
        Throwable cause = e.getCause();
        if (cause == null) {
            return ApiResult.fail(e.getMessage());
        }
        return ApiResult.fail(500, cause.toString());
    }

    /**
     * 其他错误
     */
    @ExceptionHandler({Exception.class})
    public ApiResult exception(Exception e) {
        log.error("其他错误:", e);
        return ApiResult.fail(500, "其他错误：" + e);
    }

}
