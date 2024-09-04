package org.assignment.util.extended;

import org.assignment.util.base.BaseSupplier;

public class Supplier extends BaseSupplier {
    // Shift letters and big/small + shift numbers to encrypt/decrypt
    public String encryptData (String data) {
        StringBuilder encrypted = new StringBuilder();

        for (char c : data.toCharArray()) {
            if (Character.isUpperCase(c)) {
                char switchedChar = (c == 'Z' ? 'A' : (char) (c + 1));
                encrypted.append(Character.toLowerCase(switchedChar));
            }
            else if (Character.isLowerCase(c)) {
                char switchedChar = (c == 'z' ? 'a' : (char) (c + 1));
                encrypted.append(Character.toUpperCase(switchedChar));
            }
            else if (Character.isDigit(c))
                encrypted.append(c == '9' ? '0' : (char) (c + 1));
            else
                encrypted.append(c);
        }

        return encrypted.toString();
    }

    public String decryptData (String encrypted) {
        StringBuilder decrypted = new StringBuilder();

        for (char c : encrypted.toCharArray()) {
            if (Character.isUpperCase(c)) {
                char switchedChar = (c == 'A' ? 'Z' : (char) (c - 1));
                decrypted.append(Character.toLowerCase(switchedChar));
            }
            else if (Character.isLowerCase(c)) {
                char switchedChar = (c == 'a' ? 'z' : (char) (c - 1));
                decrypted.append(Character.toUpperCase(switchedChar));
            }
            else if (Character.isDigit(c))
                decrypted.append(c == '0' ? '9' : (char) (c - 1));
            else
                decrypted.append(c);
        }

        return decrypted.toString();
    }
}

