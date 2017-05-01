lexer grammar quotation;

//@header{package pp.block1.cc.antlr;}

TOKEN
    : ('"' (~[/"])* '"')+
    ;


