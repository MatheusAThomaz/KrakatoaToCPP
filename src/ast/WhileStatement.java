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
public class WhileStatement extends Statement {

    private final Statement statement;
    private final Expr expr;

    public WhileStatement(Expr e, Statement s) {
        this.expr = e;
        this.statement = s;
    }

    public Statement getStatement() {
        return statement;
    }

    public Expr getExpr() {
        return expr;
    }    
    
    @Override
    public void genC(PW pw) {
    }

    @Override
    public void genKra(PW pw) {
        pw.printIdent("while (");
        expr.genKra(pw);
        pw.println(")");
        pw.add();
        this.statement.genKra(pw);
        pw.sub();;
    }
    
    
    
}
