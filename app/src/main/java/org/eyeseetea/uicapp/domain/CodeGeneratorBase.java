package org.eyeseetea.uicapp.domain;

import org.eyeseetea.uicapp.Utils;

import java.util.Calendar;

public abstract class CodeGeneratorBase {
    public CodeResult generateCode(Client client) {

        if (areValidAllFields(client)) {
            String code = generateCodeValue(client);

            return new CodeResult.Success(code.toUpperCase());
        } else {
            return new CodeResult.Fail();
        }
    }

    protected abstract String generateCodeValue(Client client);

    private boolean areValidAllFields(Client client) {

        return isValidMotherName(client.getMother()) &&
                isValidSurname(client.getSurname()) &&
                isValidDistrict(client.getDistrict()) &&
                isValidSex(client.getSex()) &&
                isValidTwin(client.isTwin(),client.getTwinNumber())&&
                isValidDateOfBirth(client.getDateOfBirth());
    }

    public boolean isValidMotherName(String value) {
        return isValidText(value);
    }

    public boolean isValidSurname(String value) {
        return isValidText(value);
    }

    public boolean isValidDistrict(String value) {
        return !value.isEmpty() && value.length() >= 2;
    }

    public boolean isValidSex(String value) {
        return !value.isEmpty() && value.length() >= 2;
    }

    public boolean isValidTwin(boolean isTwin, int twinNumber) {
        return !isTwin || twinNumber > 0;
    }

    public boolean isValidDateOfBirth(Long timestamp) {
        Long defaultNoDate = -1L;

        if (timestamp.equals(defaultNoDate)) {
            return false;
        }
        Calendar savedDate = Calendar.getInstance();
        savedDate.setTimeInMillis(timestamp);
        //Not pass the validation if the saved data is bigger than today.
        if (savedDate.getTimeInMillis() >= Utils.getTodayFirstTimestamp().getTime()) {
            return false;
        }
        return true;
    }

    protected boolean isValidText(String text) {
       return isValidText(text, 2);
    }

    protected boolean isValidText(String text , int minCharacters) {

        //At least two characters without numbers and with possible blank spaces
        String regExp = "\\s*(?:[A-zÀ-ÿ]\\s*){" + minCharacters +",}$";
        if (text.matches(regExp)) {
            return true;
        } else {
            return false;
        }
    }
}
