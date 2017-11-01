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
public class WritelnStatement extends Statement{

    private ExprList ex;

    public WritelnStatement(ExprList ex) {
        this.ex = ex;
    }

    @Override
    public void genC(PW pw) {
    }

    @Override
    public void genKra(PW pw) {
        pw.printIdent("writeln( ");
        
        ex.getExprList().get(0).genKra(pw);
        for(int i = 1; i < this.ex.getExprList().size(); i++)
        {
            pw.print(", ");
            ex.getExprList().get(i).genKra(pw);
        }
        pw.println(");");
    }
    
}
