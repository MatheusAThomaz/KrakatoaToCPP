/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 * @author matos
 */
public class CompositeStatement extends Statement{
    
    private StatementList st;
    
    public CompositeStatement(StatementList st){
        this.st = st;
    }

    public StatementList getSt() {
        return st;
    }

    @Override
    public void genC(PW pw) {
    }

    @Override
    public void genKra(PW pw) {
        st.genKra(pw);
    }
    
}
