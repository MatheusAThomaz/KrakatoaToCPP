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
    private ExprList exprList;
    
    public SuperExpr(MethodDec method, KraClass kraClass, ExprList expr){
        this.method = method;
        this.kraClass = kraClass;
        this.exprList = expr;
    }
    
    @Override
    public void genC(PW pw, boolean putParenthesis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Type getType() {
        return this.method.getReturnType();
    }
    
    public void genKra(PW pw)
    {
        pw.print("super." + this.method.getName() + "(");
        
        if(exprList != null)
        {
            exprList.getExprList().get(0).genKra(pw);
            for(int i = 1; i < exprList.getExprList().size(); i++)
            {
                exprList.getExprList().get(i).genKra(pw);
            }
        }
        
        pw.print(")");
    }
    
}
