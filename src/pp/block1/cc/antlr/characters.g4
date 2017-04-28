lexer grammar characters;

//@header{package pp.block1.cc.antlr;}

INT : [0-9];
ID : [a-z];
ID2 : [A-Z];
WS    : [ \t\r\n]+ -> skip ; // At least one whitespace char; don't make token

