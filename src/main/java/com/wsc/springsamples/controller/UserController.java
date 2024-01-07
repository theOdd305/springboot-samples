package com.wsc.springsamples.controller;

import com.wsc.springsamples.config.annotation.Signature;
import com.wsc.springsamples.entity.User;
import com.wsc.springsamples.entity.response.Result;
import com.wsc.springsamples.entity.validation.AddGroupValidation;
import com.wsc.springsamples.entity.validation.EditGroupValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class UserController {

    @GetMapping("/username")
    public Result<String> username() {
        return Result.success("my name is van");
    }

    @Signature
    @PostMapping("/add")
    public Result<String> add(@Validated(AddGroupValidation.class) @RequestBody User user) {
        return Result.success();
    }

    @Signature
    @PostMapping("/edit")
    public Result<String> edit(@Validated(EditGroupValidation.class) @RequestBody User user) {
        return Result.success();
    }

}
