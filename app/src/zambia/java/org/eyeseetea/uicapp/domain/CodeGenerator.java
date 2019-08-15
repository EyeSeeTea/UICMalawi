package org.eyeseetea.uicapp.domain;

public class CodeGenerator {
    public String generateCode(Client client) {

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

        return code.toUpperCase();
    }
}
