package com.bbva.hancock.sdk.models.util;

import com.bbva.hancock.sdk.exception.*;

public final class ValidateParameters{
  
  private static String addressPattern = "^(0x)?([a-fA-F0-9]{40})";
  
  public static void checkForContent(String param) throws HancockException{
    if( param.isEmpty() || param == null ){
      throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50005", 500, HancockErrorEnum.ERROR_PARAMETER.getMessage() , HancockErrorEnum.ERROR_PARAMETER.getMessage());
    }
  } 

  public static void checkAddress(String address) throws HancockException{
    if( !address.matches(addressPattern) ){
      throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50005", 500, HancockErrorEnum.ERROR_FORMAT.getMessage() , HancockErrorEnum.ERROR_FORMAT.getMessage());
    }
  } 
  
  // PRIVATE 
  private ValidateParameters(){
    //empty - prevent construction
  }
}