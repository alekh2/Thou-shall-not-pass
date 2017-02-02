package com.flipkart.depcheck.exceptions;

/**
 * Created by prasanth.narra on 17/01/17.
 */
public class InvalidScanPathException extends Exception {

    public InvalidScanPathException() {
        super();
    }

    public InvalidScanPathException(String msg) {
        super(msg);
    }

    public InvalidScanPathException(Throwable ex) {
        super(ex);
    }

    public InvalidScanPathException(String msg, Throwable ex) {
        super(msg, ex);
    }
}

