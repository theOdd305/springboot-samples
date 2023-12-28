package com.wsc.springsamples.controller;

import com.wsc.springsamples.entity.User;
import com.wsc.springsamples.entity.response.Result;
import com.wsc.springsamples.entity.validation.AddGroupValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    @GetMapping("/username")
    public Result<String> username() {
        return Result.success("my name is van");
    }

    @PostMapping("/add")
    public Result<String> add(@Validated(AddGroupValidation.class) @RequestBody User user, BindingResult bindResult) {
        if (bindResult.hasErrors()) {
            List<ObjectError> errors = bindResult.getAllErrors();
            errors.forEach(p -> {
                FieldError fieldError = (FieldError) p;
                log.error("Invalid Parameter : object - {},field - {},errorMessage - {}", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
            });
            return Result.fail("Invalid Parameter");
        }
        return Result.success();
    }

}
