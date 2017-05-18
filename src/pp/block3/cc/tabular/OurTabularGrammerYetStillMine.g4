grammar OurTabularGrammerYetStillMine;

table : BEGIN WS* row+ END;
row : column (COLUMN  column)* ROW;
column : ELEM?;

BEGIN : BACKSLASH 'begin{tabular}{' ARGS* '}';
END : BACKSLASH 'end{tabular}';
COLUMN : WS* AMPERSAND WS*;
ROW : WS* BACKSLASH BACKSLASH WS*;
ELEM : TEXT ((TEXT | WS)* TEXT)? ;
TEXT : ~[ \t\n\r\\{}&^%$#_-];
COMMENT : '%' (~[\n])+ -> skip;
ARGS : [lcr];
BACKSLASH : '\\';
AMPERSAND : '&';
WS : [ \t\n\r]+;






//tabular : '\\begin{tabular}' ARGS  data  '\\end{tabular}' ;
//data    : row+;
//row     : TEXT (AMPERSAND TEXT)* ROWSEPERATOR;
//
//AMPERSAND   : WS '&' WS;
//ROWSEPERATOR: WS '\\\\' WS;
//TEXT : [a-zA-Z0-9]*;
//
//fragment WS : [ \t\n\r]*;
//
//ARGS : '(' [ARGL]* ')';
//ARGL : [rcl];
//
//
//COMMENTS : '%' (~[\n])+ [\n] -> skip;