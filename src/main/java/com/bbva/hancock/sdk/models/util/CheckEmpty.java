package com.bbva.hancock.sdk.models.util;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.bbva.hancock.sdk.models.util.CheckEmptyImplement;

@Target( {ElementType.FIELD, ElementType.METHOD})
@Retention( RetentionPolicy.RUNTIME)
@Constraint( validatedBy = CheckEmptyImplement.class)
public @interface CheckEmpty {
  String message() default "The value is empty";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}