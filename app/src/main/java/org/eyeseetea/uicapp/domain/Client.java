package org.eyeseetea.uicapp.domain;

public class Client {
    private String mother;
    private String surname;
    private String district;
    private long dateOfBirth;
    private String sex;
    private boolean twin;
    private int twinNumber;

    public Client(String mother, String surnameKey, String district, long dateOfBirth,
            String sex, boolean twin, int twinNumber) {
        this.mother = mother;
        this.surname = surnameKey;
        this.district = district;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.twin = twin;
        this.twinNumber = twinNumber;
    }

    public String getMother() {
        return mother;
    }

    public String getSurname() {
        return surname;
    }

    public String getDistrict() {
        return district;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public boolean isTwin() {
        return twin;
    }

    public int getTwinNumber() {
        return twinNumber;
    }
}
