/*
*   Caio Henrique Giacomelli - 620297
*   Matheus Augusto Thomaz   - 620297
*/


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
    private KraClass kraclass;
    private ExprList exprList;

    
    public ThisExpr(VariableExpr var, KraClass kraclass) {
        this.method = null;
        this.var = var;
        this.kraclass = kraclass;
    }
    
    public ThisExpr(MethodDec method, KraClass kraclass, ExprList exprList) {
        this.method = method;
        this.var = null;
        this.kraclass = kraclass;
        this.exprList = exprList;
    }
    
    public ThisExpr(MethodDec method, KraClass kraclass, VariableExpr var, ExprList exprList) {
        this.method = method;
        this.var = var;
        this.kraclass = kraclass;
        this.exprList = exprList;
    }


    public ThisExpr(KraClass currentClass) {
        this.method = null;
        this.var = null;
        this.kraclass = currentClass;
    }
    
    public ThisExpr() {
        this.method = null;
        this.var = null;
        this.kraclass = null;
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
    
    public void genKra(PW pw)
    {
        
        if(var != null && this.method != null)
        {
            pw.print("this." + var.getV().getName() + "." + this.method.getName() + "(");
            
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
        else if(var == null && method != null)
        {
            pw.print("this." + this.method.getName() + "(");
            
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
        else if(var != null && method == null)
        {
            pw.print("this." + this.getVar().getV().getName());
        }
        else
            pw.print("this");
        
    }
    
    @Override
    public void genKraIdented(PW pw)
    {
        
        if(var != null && this.method != null)
        {
            pw.printIdent("this." + var.getV().getName() + "." + this.method.getName() + "(");
            
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
        else if(var == null && method != null)
        {
            pw.printIdent("this." + this.method.getName() + "(");
            
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
        else if(var != null && method == null)
        {
            pw.printIdent("this." + this.getVar().getV().getName());
        }
        else
            pw.printIdent("this");
        
    }

    @Override
    public Type getType() {
        
        if (method != null){
            return this.method.getReturnType();
        }
        else if (this.var != null)
            return this.var.getType();
        else return new TypeNull();
        
    }
    
    
    
}
