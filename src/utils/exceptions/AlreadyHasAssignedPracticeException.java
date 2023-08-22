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
public class AlreadyHasAssignedPracticeException extends Exception {
    
    public AlreadyHasAssignedPracticeException(String errorMessage) {
        super(errorMessage);
    }
    
    public AlreadyHasAssignedPracticeException() {
    }
    
}
