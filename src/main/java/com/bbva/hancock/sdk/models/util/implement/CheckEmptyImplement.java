package com.bbva.hancock.sdk.models.util.implement;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.exception.HancockTypeErrorEnum;
import com.bbva.hancock.sdk.models.util.CheckEmpty;

public class CheckEmptyImplement implements ConstraintValidator<CheckEmpty, String> {

  @Override
  public void initialize( final CheckEmpty constraintAnnotation) {
    // nothing to do.
  }

  @Override
  public boolean isValid( final String param, final ConstraintValidatorContext context) {
//    if( param.isEmpty() || param == null ){ 
      throw new ValidationException("Empty parameters");
////      throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50005", 500, HancockErrorEnum.ERROR_PARAMETER.getMessage() , HancockErrorEnum.ERROR_PARAMETER.getMessage());
//      //return false;
//    }
//    return false;
  }
}