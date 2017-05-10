lexer grammar CC2;

//L : R 'a' | Q 'b' 'a';
//fragment R : 'aba' Rprime | 'caba' Rprime;
//fragment Rprime :  'bc' Rprime | ;
//fragment Q : 'b' S;
//fragment S : 'bc' | 'c';

A : 'a';
B : 'b';
Ba :  'ba';
Aba : 'aba';
Caba : 'caba';
Bc : 'bc';
C : 'c';

// ignore whitespace
WS : [ \t\n\r] -> skip;

// everything else is a typo
TYPO : [a-zA-Z]+;
