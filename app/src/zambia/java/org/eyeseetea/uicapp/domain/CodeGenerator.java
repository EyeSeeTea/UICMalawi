package org.eyeseetea.uicapp.domain;

public class CodeGenerator extends CodeGeneratorBase{
    @Override
    protected String generateCodeValue(Client client) {
        String code = "";

        code += GeneratorUtils.extratcSecondAndThirdtLetters(client.getMother());

        code += GeneratorUtils.extratcSecondAndThirdtLetters(client.getSurname());

        code += GeneratorUtils.extractLastLetters(client.getDistrict(),2);

        code += GeneratorUtils.formatDate(client.getDateOfBirth());
        code += GeneratorUtils.extractFirstLetters(client.getSex(), 1);

        if (client.isTwin()) {
            code += "T" + client.getTwinNumber();
        }

        return code;
    }

    @Override
    public boolean isValidMotherName(String value) {
        return isValidText(value,3);
    }

    @Override
    public boolean isValidSurname(String value) {
        return isValidText(value,3);
    }
}
