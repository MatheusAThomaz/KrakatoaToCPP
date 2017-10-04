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
public class WriteStatement extends Statement{

    private ExprList ex;
    
    public WriteStatement(ExprList ex) {
        this.ex = ex;
    }

    @Override
    public void genC(PW pw) {
    }

    @Override
    public void genKra(PW pw) {
    }
    
}
