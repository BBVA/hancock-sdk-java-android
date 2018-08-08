package com.bbva.hancock.sdk.models.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

public class CheckEmptyImplement implements ConstraintValidator<CheckEmpty, String> {
  private String param;

  @Override
  public void initialize( CheckEmpty param) {
    this.param = "";
  }
  @Override
  public boolean isValid( String param, ConstraintValidatorContext context) {
    if( param.isEmpty() || param == null ){
      throw new ValidationException("Empty parameters");
    }
    return true;
  }
}