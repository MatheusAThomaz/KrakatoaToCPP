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
public class ReadStatement extends Statement{
    
    private ArrayList<String> nameList;
    
    public ReadStatement(){
        nameList = new ArrayList();
    }
    
    public void addNameList(String name){
        nameList.add(name);
    }

    @Override
    public void genC(PW pw) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void genKra(PW pw) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
