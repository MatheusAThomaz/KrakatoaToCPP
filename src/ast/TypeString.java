/*
*   Caio Henrique Giacomelli - 620297
*   Matheus Augusto Thomaz   - 620297
*/


 package ast;

public class TypeString extends Type {
    
    public TypeString() {
        super("string");
    }
    
   public String getCname() {
      return "char *";
   }

}