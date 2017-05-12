grammar CC10;

expression: '(' plus ')';

plus : <assoc=left> plus PLUS plus             #plusExpression
     | minus                                   #plusLink
     ;

minus : <assoc=left> minus MINUS minus         #minusExpression
      | mult                                   #minusLink
      ;

mult: <assoc=left> mult MULT mult              #multExpression
    | div                                      #multLink
    ;

div: <assoc=left> div DIV div                  #divExpression
    | pow                                      #divLink
    ;

pow: <assoc=right> pow POW pow                 #powExpression
     | expression                              #expressionLoop
     | MINUS minus                             #negative
     | NUM                                     #num
     ;

POW : '^' ;
DIV : '/' ;
MULT : '*' ;
PLUS : '+' ;
MINUS : '-' ;
NUM : [0-9]+;

// ignore whitespace
WS : [ \t\n\r] -> skip;

// everything else is a typo
TYPO : [a-zA-Z]+;
