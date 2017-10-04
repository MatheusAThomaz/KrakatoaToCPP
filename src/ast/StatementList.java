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
public class StatementList {
    public StatementList() {
        stmtList = new ArrayList<Statement>();
    }

    public void addElement( Statement stmt ) {
        stmtList.add(stmt);
    }

    private ArrayList<Statement> stmtList;
    
}
