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
public class PracticeAlreadyAssignedException extends Exception {
    
    public PracticeAlreadyAssignedException(String errorMessage) {
        super(errorMessage);
    }
    
    public PracticeAlreadyAssignedException() {
    }
}
