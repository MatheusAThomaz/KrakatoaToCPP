/*
*   Caio Henrique Giacomelli - 620297
*   Matheus Augusto Thomaz   - 620297
*/


 package ast;

public class TypeBoolean extends Type {

   public TypeBoolean() { super("bool"); }

   @Override
   public String getCname() {
      return "int";
   }

}
