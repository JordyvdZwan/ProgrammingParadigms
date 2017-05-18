grammar TT;

import TTVocab;

ttt : <assoc=right> ttt  POWER   ttt   # power
     | ttt  PLUS    ttt   # plus
     | ttt  EQUALS  ttt   # equals
     | LPAR ttt     RPAR  # par
     | TYPE               # type
     ;
