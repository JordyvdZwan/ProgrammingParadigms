grammar OurTabularGrammerYetStillMine;

tabular : '\\begin{tabular}' ARGS  data  '\\end{tabular}' ;
data    : row+;
row     : TEXT (AMPERSAND TEXT)* ROWSEPERATOR;

AMPERSAND   : WS '&' WS;
ROWSEPERATOR: WS '\\\\' WS;
TEXT : [a-zA-Z0-9]*;

fragment WS : [ \t\n\r]*;

ARGS : '(' [ARGL]* ')';
ARGL : [rcl];


COMMENTS : '%' (~[\n])+ [\n] -> skip;