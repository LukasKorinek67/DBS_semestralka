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
public class InvalidCompanyCountryChoiceException extends Exception{
    
    public InvalidCompanyCountryChoiceException(String errorMessage) {
        super(errorMessage);
    }
    
    public InvalidCompanyCountryChoiceException() {
    }
    
}
