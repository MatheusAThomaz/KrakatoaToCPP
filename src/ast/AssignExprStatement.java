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

public class AssignExprStatement extends Statement{
    Expr leftside;
    Expr rightside;
    Boolean isLocalDec;
    
    public AssignExprStatement(Expr l, Expr r, Boolean isLocalDec){
        this.leftside = l;
        this.rightside = r;
        this.isLocalDec = isLocalDec;
    }
    
    public void genKra(PW pw){
        
        if (isLocalDec){
            pw.printIdent(leftside.getType().getName() + " ");
            leftside.genKra(pw);
            pw.println(";");
        }
        else{
            
            leftside.genKraIdented(pw);
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
