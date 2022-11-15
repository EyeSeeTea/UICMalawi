package org.eyeseetea.uicapp.domain;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CodeGeneratorShould {
    CodeGenerator codeGenerator = new CodeGenerator();

    @Test
    public void return_invalid_code_if_mother_name_client_is_not_valid() {
        Client invalidClient = givenAInvalidClientByMotherName();

        CodeResult codeResult = codeGenerator.generateCode(invalidClient);

        Assert.assertTrue(codeResult instanceof CodeResult.Fail);
        Assert.assertFalse(codeResult.isValid());
        Assert.assertFalse(codeGenerator.isValidMotherName(invalidClient.getMother()));
    }

    @Test
    public void return_invalid_code_if_surname_client_is_not_valid() {
        Client invalidClient = givenAInvalidClientBySurname();

        CodeResult codeResult = codeGenerator.generateCode(invalidClient);

        Assert.assertTrue(codeResult instanceof CodeResult.Fail);
        Assert.assertFalse(codeResult.isValid());
        Assert.assertFalse(codeGenerator.isValidSurname(invalidClient.getSurname()));
    }

    @Test
    public void return_invalid_code_if_district_client_is_not_valid() {
        Client invalidClient = givenAInvalidClientByDistrict();

        CodeResult codeResult = codeGenerator.generateCode(invalidClient);

        Assert.assertTrue(codeResult instanceof CodeResult.Fail);
        Assert.assertFalse(codeResult.isValid());
        Assert.assertFalse(codeGenerator.isValidDistrict(invalidClient.getDistrict()));
    }

    @Test
    public void return_invalid_code_if_date_of_birth_client_is_not_valid() {
        Client invalidClient = givenAInvalidClientByDateOfBirth();

        CodeResult codeResult = codeGenerator.generateCode(invalidClient);

        Assert.assertTrue(codeResult instanceof CodeResult.Fail);
        Assert.assertFalse(codeResult.isValid());
        Assert.assertFalse(codeGenerator.isValidDateOfBirth(invalidClient.getDateOfBirth()));
    }

    @Test
    public void return_invalid_code_if_sex_client_is_not_valid() {
        Client invalidClient = givenAInvalidClientBySex();

        CodeResult codeResult = codeGenerator.generateCode(invalidClient);

        Assert.assertTrue(codeResult instanceof CodeResult.Fail);
        Assert.assertFalse(codeResult.isValid());
        Assert.assertFalse(codeGenerator.isValidSex(invalidClient.getSex()));
    }

    @Test
    public void return_invalid_code_if_twin_client_is_not_valid() {
        Client invalidClient = givenAInvalidClientByTwin();

        CodeResult codeResult = codeGenerator.generateCode(invalidClient);

        Assert.assertTrue(codeResult instanceof CodeResult.Fail);
        Assert.assertFalse(codeResult.isValid());
        Assert.assertFalse(
                codeGenerator.isValidTwin(invalidClient.isTwin(), invalidClient.getTwinNumber()));
    }


    @Test
    public void return_invalid_code_if_mother_name_and_surname_contains_numbers() {
        Client invalidClient = givenAInvalidClientWithNumbers();

        CodeResult codeResult = codeGenerator.generateCode(invalidClient);

        Assert.assertTrue(codeResult instanceof CodeResult.Fail);
        Assert.assertFalse(codeResult.isValid());
        Assert.assertFalse(codeGenerator.isValidMotherName(invalidClient.getMother()));
        Assert.assertFalse(codeGenerator.isValidSurname(invalidClient.getMother()));
    }


    @Test
    public void return_valid_code_if_is_not_twin() {
        Client noTwinClient = givenAValidClient(0);

        CodeResult codeResult = codeGenerator.generateCode(noTwinClient);

        Assert.assertTrue(codeResult instanceof CodeResult.Success);
        Assert.assertTrue(codeResult.isValid());
        Assert.assertEquals("ARAMBA020275F", ((CodeResult.Success)codeResult).getCode());
    }

    @Test
    public void return_valid_code_if_is_twin() {
        Client twinClient = givenAValidClient(1);

        CodeResult codeResult = codeGenerator.generateCode(twinClient);

        Assert.assertTrue(codeResult instanceof CodeResult.Success);
        Assert.assertTrue(codeResult.isValid());
        Assert.assertEquals("ARAMBA020275FT1", ((CodeResult.Success)codeResult).getCode());
    }

    @Test
    public void return_valid_code_if_surname_is_multiple() {
        Client twinClient = givenAValidClientWithMultipleSurnames();

        CodeResult codeResult = codeGenerator.generateCode(twinClient);

        Assert.assertTrue(codeResult instanceof CodeResult.Success);
        Assert.assertTrue(codeResult.isValid());
        Assert.assertEquals("ARAMBA020275F", ((CodeResult.Success) codeResult).getCode());
    }

    private Client givenAInvalidClientByMotherName() {
        Client validClient = givenAValidClient(0);

        Client invalidClient = new Client("aa", validClient.getSurname(),
                validClient.getDistrict(), validClient.getDateOfBirth(), validClient.getSex(),
                validClient.isTwin(), validClient.getTwinNumber());

        return invalidClient;
    }

    private Client givenAInvalidClientBySurname() {
        Client validClient = givenAValidClient(0);

        Client invalidClient = new Client(validClient.getMother(), "aa",
                validClient.getDistrict(), validClient.getDateOfBirth(), validClient.getSex(),
                validClient.isTwin(), validClient.getTwinNumber());

        return invalidClient;
    }

    private Client givenAInvalidClientByDistrict() {
        Client validClient = givenAValidClient(0);

        Client invalidClient = new Client(validClient.getMother(), validClient.getSurname(),
                "", validClient.getDateOfBirth(), validClient.getSex(),
                validClient.isTwin(), validClient.getTwinNumber());

        return invalidClient;
    }

    private Client givenAInvalidClientByDateOfBirth() {
        Client validClient = givenAValidClient(0);

        Client invalidClient = new Client(validClient.getMother(), validClient.getSurname(),
                validClient.getDistrict(), new Date().getTime(), validClient.getSex(),
                validClient.isTwin(), validClient.getTwinNumber());

        return invalidClient;
    }

    private Client givenAInvalidClientBySex() {
        Client validClient = givenAValidClient(0);

        Client invalidClient = new Client(validClient.getMother(), validClient.getSurname(),
                validClient.getDistrict(), validClient.getDateOfBirth(), "",
                validClient.isTwin(), validClient.getTwinNumber());

        return invalidClient;
    }

    private Client givenAInvalidClientByTwin() {
        Client validClient = givenAValidClient(0);

        Client invalidClient = new Client(validClient.getMother(), validClient.getSurname(),
                validClient.getDistrict(), validClient.getDateOfBirth(), validClient.getSex(),
                true, validClient.getTwinNumber());

        return invalidClient;
    }

    private Client givenAInvalidClientWithNumbers() {
        Client validClient = givenAValidClient(0);

        Client invalidClient = new Client(validClient.getMother() + "88",
                validClient.getSurname()+ "88", validClient.getDistrict(),
                validClient.getDateOfBirth(), validClient.getSex(), validClient.isTwin(),
                validClient.getTwinNumber());

        return invalidClient;
    }

    private Client givenAValidClientWithMultipleSurnames() {
        Client validClient = givenAValidClient(0);

        Client client = new Client(validClient.getMother(),"James Ruohi",
                validClient.getDistrict(), validClient.getDateOfBirth(), validClient.getSex(),
                validClient.isTwin(), validClient.getTwinNumber());

        return client;
    }

    private Client givenAValidClient(int twinNumber) {
        String mother = "Sarah";
        String surname = "James";
        String district = "Zomba";

        Long dateOfBirth = new GregorianCalendar(1975, Calendar.FEBRUARY, 2).getTime().getTime();

        String sex = "FEMALE";

        boolean isTwin = (twinNumber > 0);

        return new Client(mother, surname, district, dateOfBirth, sex, isTwin, twinNumber);
    }
}