package org.btc.exceptions;

public class YamlPathNotFoundException extends Exception{
    public YamlPathNotFoundException(String notice){
        super(notice);
    }
}
