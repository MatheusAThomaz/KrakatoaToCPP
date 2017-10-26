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
public class ThisExpr extends Expr {
    
    private MethodDec method;
    private VariableExpr var;
    private VariableExpr var2;
    private KraClass kraclass;

    public ThisExpr(MethodDec method, VariableExpr var, VariableExpr var2, KraClass kraclass) {
        this.method = method;
        this.var = var;
        this.var2 = var2;
        this.kraclass = kraclass;
    }
    
    public ThisExpr(VariableExpr var, KraClass kraclass) {
        this.method = null;
        this.var = var;
        this.var2 = null;
        this.kraclass = kraclass;
    }

    public ThisExpr(KraClass currentClass) {
        this.method = null;
        this.var = null;
        this.var2 = null;
        this.kraclass = currentClass;
    }
    
    public ThisExpr() {
        this.method = null;
        this.var = null;
        this.var2 = null;
        this.kraclass = null;
    }

    public VariableExpr getVar2() {
        return var2;
    }

    public MethodDec getMethod() {
        return method;
    }

    public VariableExpr getVar() {
        return var;
    }

    @Override
    public void genC(PW pw, boolean putParenthesis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Type getType() {
        
        if (method != null){
            return this.method.getReturnType();
        }
        else if (this.var2 != null)
            return this.var2.getType();
        else if (this.var != null)
            return this.var.getType();
        else return new TypeNull();
        
    }
    
    
    
}
