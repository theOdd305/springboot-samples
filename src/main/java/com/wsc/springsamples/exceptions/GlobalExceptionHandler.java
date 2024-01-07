package com.wsc.springsamples.exceptions;

import com.wsc.springsamples.entity.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { BindException.class, ValidationException.class, MethodArgumentNotValidException.class })
    public Result<ExceptionData> handleParameterVerificationException(@NonNull Exception e) {
        ExceptionData.ExceptionDataBuilder exceptionDataBuilder = ExceptionData.builder();
        log.warn("Exception: {}", e.getMessage());
        if (e instanceof BindException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .forEach(exceptionDataBuilder::error);
            List<ObjectError> errors = bindingResult.getAllErrors();
            errors.forEach(p -> {
                FieldError fieldError = (FieldError) p;
                log.error("Invalid Parameter : object - {},field - {},errorMessage - {}", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
            });
        } else if (e instanceof ConstraintViolationException) {
            if (e.getMessage() != null) {
                exceptionDataBuilder.error(e.getMessage());
            }
        } else {
            exceptionDataBuilder.error("invalid parameter");
        }
        return Result.fail(exceptionDataBuilder.build(), "invalid parameter");
    }

    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public Result<BusinessException> businessException(BusinessException e) {
        log.error(e.getLocalizedMessage(), e);
        return Result.fail(e.getLocalizedMessage());
    }

    /**
     * handle other exception.
     *
     * @param exception
     *            exception
     * @return ResponseResult
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result<Exception> processException(Exception exception) {
        log.error(exception.getLocalizedMessage(), exception);
        // 这里可以屏蔽掉后台的异常栈信息，直接返回"server error"
        return Result.fail(exception, exception.getLocalizedMessage());
    }

}
