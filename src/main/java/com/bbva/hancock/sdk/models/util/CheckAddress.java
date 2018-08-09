package com.bbva.hancock.sdk.models.util;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.bbva.hancock.sdk.models.util.implement.CheckAddressImplement;

@Target( {ElementType.PARAMETER, ElementType.ANNOTATION_TYPE} )
@Retention( RetentionPolicy.RUNTIME )
@Constraint( validatedBy = {CheckAddressImplement.class})
@Documented
public @interface CheckAddress {
  String message() default "The address has bad format";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
