lexer grammar TTVocab;

POWER  : '^';
PLUS   : '+';
EQUALS : '=';
LPAR   : '(';
RPAR   : ')';

TYPE : 'NUM' | 'STR' | 'BOOL';

// ignore whitespace
WS : [ \t\n\r] -> skip;
