package org.eyeseetea.uicapp.domain;

public abstract class CodeResult {
    public abstract boolean isValid();

    public static class Success extends CodeResult {
        public String code;

        public Success(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        @Override
        public boolean isValid() {
            return true;
        }
    }

    public static class Fail extends CodeResult{
        @Override
        public boolean isValid() {
            return false;
        }
    }
}

