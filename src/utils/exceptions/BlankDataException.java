/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.exceptions;

/**
 *
 * @author lukaskorinek
 */
public class BlankDataException extends Exception {
    
    public BlankDataException(String errorMessage) {
        super(errorMessage);
    }
    
    public BlankDataException() {
    }
}
