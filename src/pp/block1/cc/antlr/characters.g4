lexer grammar characters;

//@header{package pp.block1.cc.antlr;}

ACCEPTING : ALPHABETICAL TEXT TEXT TEXT TEXT TEXT;
fragment ALPHABETICAL : LOWER | UPPER;
fragment TEXT: DIGITS | LOWER | UPPER;
fragment DIGITS : '0'..'9';
fragment LOWER : 'a'..'z';
fragment UPPER : 'A'..'Z';
WS    : [ \t\r\n]+ -> skip ; // At least one whitespace char; don't make token

