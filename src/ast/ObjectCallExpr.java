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
    private Variable variable2;
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
    
    public Variable getVariable2() {
        return variable2;
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
        this.variable2 = null;
        
    }
    
    public ObjectCallExpr (Variable variable, String className)
    {
        this.method = null;
        this.className = className;
        this.variable = variable;
        this.expr = null;
        this.variable2 = null;
    }
    
    public ObjectCallExpr (Variable variable, Variable variable2, String className)
    {
        this.method = null;
        this.className = className;
        this.variable = variable;
        this.expr = null;
        this.variable2 = variable2;
    }
    
    
    @Override
    public void genC(PW pw, boolean putParenthesis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Type getType() {
        
        if (this.method == null){
            if (this.variable2 == null) return this.variable.getType();
            else return this.variable2.getType();
        }
        else return this.method.getReturnType();
        
    }
    
    
}
