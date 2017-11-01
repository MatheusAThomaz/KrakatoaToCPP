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
public class StatementList extends Statement{

    public ArrayList<Statement> getStmtList() {
        return stmtList;
    }
    public StatementList() {
        stmtList = new ArrayList<Statement>();
    }

    public void addElement( Statement stmt ) {
        stmtList.add(stmt);
    }

    private ArrayList<Statement> stmtList;

    @Override
    public void genC(PW pw) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void genKra(PW pw) {
        for(Statement st : stmtList){
            if (st != null) st.genKra(pw);
        }
    }
    
}
