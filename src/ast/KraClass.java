package ast;

import java.util.ArrayList;

/*
 * Krakatoa Class
 */
public class KraClass extends Type {
	
   public KraClass( String name ) {
      super(name);
      publicMethodList = new ArrayList();
   }
   
   public String getCname() {
      return getName();
   }
   
    boolean isSubclasOf(Type other) {
        KraClass current = this;
        while( current != other)
        {
            current = current.getSuperClass();
            
            if(current == null)
            {
                return false;
            }
        }
        return true;
    }
    
   private KraClass getSuperClass() {
       return superclass;
   }
    
   private void getSuperClass(KraClass superclass) {
       this.superclass = superclass;   
   }
   
   public void addMethod(MethodDec aMethod){
       publicMethodList.add(aMethod);
   }
   
   public MethodDec searchPublicMethod(String methodName) {
        for (MethodDec m : this.publicMethodList){
            if (m.getName().equals(methodName)){
                return m;
            }
        }
       
       return null;
   }
   
   public void addInstanceVariable(InstanceVariable instanceVariable) {
       this.instanceVar.add(instanceVariable);
   }
   
   private String name;
   private KraClass superclass;
   private InstanceVariableList instanceVariableList;
   private ArrayList<MethodDec> publicMethodList;
   private ArrayList<InstanceVariable> instanceVar;
   // private MethodList publicMethodList, privateMethodList;
   // m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
   // entre outros m�todos

  






}
