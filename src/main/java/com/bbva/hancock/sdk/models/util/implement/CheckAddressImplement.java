package com.bbva.hancock.sdk.models.util.implement;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import com.bbva.hancock.sdk.models.util.CheckAddress;
import com.bbva.hancock.sdk.models.util.CheckEmpty;

public class CheckAddressImplement implements ConstraintValidator<CheckAddress, String> {

  private static String addressPattern = "^(0x)?([a-fA-F0-9]{40})";
  
  @Override
  public void initialize( final CheckAddress constraintAnnotation) {
    // nothing to do.
  }

  @Override
  public boolean isValid( final String address, final ConstraintValidatorContext context) {
    if( !address.matches(addressPattern) ){
      //throw new ValidationException("Address bad format");
      return false;
    }
    return true;
  }
}