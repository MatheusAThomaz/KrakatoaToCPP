/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import lexer.Symbol;

/**
 *
 * @author matos
 */
public class MethodDec {

    private final String name;
    private final Type returnType;
    private final Symbol qualifier;

    public MethodDec(String name, Type returnType, Symbol qualifier) {
        this.name = name;
        this.returnType = returnType;
        this.qualifier = qualifier;    
    }    

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Symbol getQualifier() {
        return qualifier;
    }
    
}
