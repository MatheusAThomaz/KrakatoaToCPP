/**
  
 */
package ast;

import java.util.*;

/** This class represents a metaobject call as <code>{@literal @}ce(...)</code> in <br>
 * <code>
 * @ce(5, "'class' expected") <br>
 * clas Program <br>
 *     public void run() { } <br>
 * end <br>
 * </code>
 * 
   @author Jos�
   
 */
public class MetaobjectCall {

	public MetaobjectCall(String name, ArrayList<Object> paramList) {
		this.name = name;
		this.paramList = paramList;
	}
	
        public void genKra(PW pw)
        {
            pw.println("@"+name);
            
            for(Object obj : this.getParamList())
            {
                    pw.println(obj.toString());
            }
            
            pw.println();
        }
        
	public ArrayList<Object> getParamList() {
		return paramList;
	}
	public String getName() {
		return name;
	}


	private String name;
	private ArrayList<Object> paramList;

}
