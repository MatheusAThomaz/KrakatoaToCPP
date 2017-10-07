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
public class ObjectCallExpr extends Expr {
    
    private MethodDec method;
    private String className;
    private Variable variable;
    private ExprList expr;

    public MethodDec getMethod() {
        return method;
    }

    public String getClassName() {
        return className;
    }

    public Variable getVariable() {
        return variable;
    }

    public ExprList getExpr() {
        return expr;
    }
    
    
    public ObjectCallExpr(MethodDec method, String className, ExprList expr)
    {
        this.method = method;
        this.className = className;
        this.variable = null;
        this.expr = expr;
    }
    
    public ObjectCallExpr (Variable variable, String className)
    {
        this.method = null;
        this.className = className;
        this.variable = variable;
    }
    
    @Override
    public void genC(PW pw, boolean putParenthesis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Type getType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
