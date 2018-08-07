package com.bbva.hancock.sdk.models.util;

import com.bbva.hancock.sdk.exception.*;

public final class ValidateParameters{
  
  public static void checkForContent(String param) throws HancockException{
    if( param.isEmpty() | param == null ){
      throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50005", 500, HancockErrorEnum.ERROR_PARAMETER.getMessage() , HancockErrorEnum.ERROR_PARAMETER.getMessage());
    }
  }  
  
  // PRIVATE 
  private ValidateParameters(){
    //empty - prevent construction
  }
}