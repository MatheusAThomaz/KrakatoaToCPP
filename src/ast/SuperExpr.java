/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 * @author maiks
 */
public class SuperExpr extends Expr {

    private MethodDec method;
    private KraClass kraClass;
    
    public SuperExpr(MethodDec method, KraClass kraClass){
        this.method = method;
        this.kraClass = kraClass;
    }
    
    @Override
    public void genC(PW pw, boolean putParenthesis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Type getType() {
        return this.method.getReturnType();
    }
    
}
