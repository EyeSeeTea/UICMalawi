package org.eyeseetea.uicapp.domain;

public class CodeGenerator extends CodeGeneratorBase{
    @Override
    protected String generateCodeValue(Client client) {
        String code = "";

        code += GeneratorUtils.extractLetters(client.getMother(), 1,3);

        code += GeneratorUtils.extractLetters(client.getSurname(),1,3);

        code += GeneratorUtils.extractLetters(client.getDistrict(),
                client.getDistrict().length() - 2);

        code += GeneratorUtils.formatDate(client.getDateOfBirth());
        code += GeneratorUtils.extractLetters(client.getSex(), 0, 1);

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
