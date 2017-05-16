grammar TT;

import TTVocab;

ttt :  ttt  POWER   ttt   # power
     | ttt  PLUS    ttt   # plus
     | ttt  EQUALS  ttt   # equals
     | LPAR ttt     RPAR  # par
     | TYPE               # type
     ;
