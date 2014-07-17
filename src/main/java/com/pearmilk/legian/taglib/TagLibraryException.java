package com.pearmilk.legian.taglib;

/**
 * Created with IntelliJ IDEA.
 * User: matteopelati
 * Date: 9/9/13
 * Time: 6:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class TagLibraryException extends Exception {
    public TagLibraryException(String message) {
        super(message);
    }

    public TagLibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}
