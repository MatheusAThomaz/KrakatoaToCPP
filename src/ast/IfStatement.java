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
public class IfStatement extends Statement{

    private Expr e;
    private Statement st;
    private Statement elsest;

    public IfStatement(Expr e, Statement st, Statement elsest) {
        this.e = e;
        this.st = st;
        this.elsest = st;
    }

    @Override
    public void genC(PW pw) {
    }

    @Override
    public void genKra(PW pw) {
    }
    
}
