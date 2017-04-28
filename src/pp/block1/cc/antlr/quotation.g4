lexer grammar quotation;

//@header{package pp.block1.cc.antlr;}

ACCEPTING : QUOTATION STRING* (QUOTATION QUOTATION)* STRING* QUOTATION;
fragment QUOTATION : '"';
fragment STRING : '\u0000'..'\u00FF' ~["];
WS  : [ \t\r\n]+ -> skip ; // At least one whitespace char; don't make token

