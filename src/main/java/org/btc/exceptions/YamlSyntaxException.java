package org.btc.exceptions;

import org.yaml.snakeyaml.scanner.ScannerException;

public class YamlSyntaxException extends Exception {
    ScannerException e;
    public YamlSyntaxException(String notice, ScannerException e) {
        super(notice);
        this.e = e;
    }

    @Override
    public void printStackTrace() {
        System.err.println(this);
        e.printStackTrace();
    }
}
