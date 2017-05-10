lexer grammar CC1;

//@header{package pp.block2.cc.ll;}

Assign : 'assign';
If : 'if';
Expr : 'expr';
Then : 'then';
Else : 'else';

// ignore whitespace
WS : [ \t\n\r] -> skip;

// everything else is a typo
TYPO : [a-zA-Z]+;
