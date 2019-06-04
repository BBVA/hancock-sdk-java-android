package com.bbva.hancock.sdk.config;

import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.util.ValidateParameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ValidateParametersTest {

    @Test(expected = HancockException.class)
    public void checkForContent() throws HancockException {
        ValidateParameters.checkForContent(null, null);
    }

    @Test
    public void normalizeAlias() {
        final String alias = ValidateParameters.normalizeAlias("ALIAS");
        Assert.assertEquals(alias, "alias");
    }

    @Test
    public void normalizeAddress() {
        String address = ValidateParameters.normalizeAlias("0x1234");
        Assert.assertEquals(address, "0x1234");
        address = ValidateParameters.normalizeAlias("1234");
        Assert.assertEquals(address, "1234");
    }

    @Test
    public void normalizeAdressOrAlias() {
        String address = ValidateParameters.normalizeAdressOrAlias("0x1234");
        Assert.assertEquals(address, "0x1234");
        address = ValidateParameters.normalizeAdressOrAlias("1234");
        Assert.assertEquals(address, "1234");
    }


}
