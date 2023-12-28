package com.wsc.springsamples.entity;

import com.wsc.springsamples.entity.validation.EditGroupValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @NotEmpty(message = "could not be empty", groups = {EditGroupValidation.class})
    private String userId;

    @Length(max = 50, message = "A maximum of 50 characters can be entered")
    private String name;

    @Range(min = 0, max = 200, message = "age should be 0 ~ 200")
    private Integer age;

    @Length(max = 200, message = "A maximum of 100 characters can be entered")
    private String remarks;

}
