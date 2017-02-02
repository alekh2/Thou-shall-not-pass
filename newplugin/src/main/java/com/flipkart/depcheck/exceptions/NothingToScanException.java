package com.flipkart.depcheck.exceptions;

/**
 * Created by prasanth.narra on 17/01/17.
 */
public class NothingToScanException extends Exception{

    public NothingToScanException() {
        super("No path provided for scanning");
    }
}
