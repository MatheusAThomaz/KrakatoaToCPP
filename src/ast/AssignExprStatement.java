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

import java.util.ArrayList;

/**
 *
 * @author matos
 */

public class AssignExprStatement extends Statement{
    Expr leftside;
    Expr rightside;
    ArrayList<VariableExpr> vListExpr;
    Boolean isLocalDec;
    
    public AssignExprStatement(Expr l, Expr r, Boolean isLocalDec){
        this.leftside = l;
        this.rightside = r;
        this.isLocalDec = isLocalDec;
        this.vListExpr = null;
    }
    
    public AssignExprStatement(ArrayList<VariableExpr> vListExpr, Expr r, Boolean isLocalDec){
        this.leftside = null;
        this.rightside = r;
        this.isLocalDec = isLocalDec;
        this.vListExpr = vListExpr;
    }
    
    public void genKra(PW pw){
        
        if (isLocalDec){
            if (vListExpr != null) pw.printIdent(vListExpr.get(0).getType().getName() + " ");

            
            if (vListExpr != null){
                
                if(vListExpr.get(0).getType() instanceof KraClass || vListExpr.get(0).getType().getName().equals("string")){
                    pw.print("*");
                    vListExpr.get(0).genKra(pw);
                }
                else
                    vListExpr.get(0).genKra(pw);
                
                for (int i = 1; i < vListExpr.size(); i++){
                    pw.print(", ");
                    if(vListExpr.get(i).getType() instanceof KraClass || vListExpr.get(i).getType().getName().equals("string")){
                        
                        pw.print("*");
                        vListExpr.get(i).genKra(pw);
                    }
                    else
                        vListExpr.get(i).genKra(pw);
                }
            }
            pw.println(";");
        }
        else{
            if (leftside.getType().getName().equals("string")){
                pw.printIdent("");
                leftside.genKraIdented(pw, true);
                }
            else
                leftside.genKraIdented(pw, false);
            
            if (rightside != null){
                pw.print(" = ");
                rightside.genKra(pw);
                pw.println(";");
            }
            
            if(rightside == null)
                pw.println(";");
            
            
        }
    }
    
    

    @Override
    public void genC(PW pw) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}