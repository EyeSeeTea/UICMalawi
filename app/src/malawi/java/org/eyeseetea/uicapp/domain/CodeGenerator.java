package org.eyeseetea.uicapp.domain;

public class CodeGenerator extends CodeGeneratorBase {

    @Override
    protected String generateCodeValue(Client client) {

        String code = "";

        code += GeneratorUtils.extractLastLetters(client.getMother(), 2);

        code += GeneratorUtils.extractLastLetters(client.getSurname(), 2);

        code += GeneratorUtils.extractLastLetters(client.getDistrict(), 2);

        code += GeneratorUtils.formatDate(client.getDateOfBirth());

        code += GeneratorUtils.extractFirstLetters(client.getSex(),  1);

        code += client.getTwinNumber();

        return code;
    }
}
