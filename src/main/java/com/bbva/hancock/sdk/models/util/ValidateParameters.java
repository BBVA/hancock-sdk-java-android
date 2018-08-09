package com.bbva.hancock.sdk.models.util;

import com.bbva.hancock.sdk.exception.*;

public final class ValidateParameters{
  
  private static String addressPattern = "^(0x)?([a-fA-F0-9]{40})";
  private static String message = " is empty";
  
  public static void checkForContent(String param, String var) throws HancockException{
    if( param.isEmpty() || param == null ){
      throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50005", 500, HancockErrorEnum.ERROR_PARAMETER.getMessage() , var + message);
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