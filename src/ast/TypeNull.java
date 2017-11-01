/*
*   Caio Henrique Giacomelli - 620297
*   Matheus Augusto Thomaz   - 620297
*/


 package ast;

public class TypeNull extends Type {
    
    public TypeNull() {
        super("null");
    }
    
   public String getCname() {
      return "null";
   }

}