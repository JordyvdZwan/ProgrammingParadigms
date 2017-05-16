grammar TTAttr;

import TTVocab;

@members {
    private Type getValue(String text) {
        switch (text) {
            case "NUM":
                return Type.NUM;
            case "STR":
                return Type.STR;
            case "BOOL":
                return Type.BOOL;
        }
        return Type.ERR;
    }
}

ttt returns [ Type val ]
     : t0=ttt POWER t1=ttt
       { $val = ($t0.val == Type.NUM && $t1.val == Type.NUM) ? Type.NUM : (($t0.val == Type.STR && $t1.val == Type.NUM) ? Type.STR : Type.ERR); }
     | t0=ttt PLUS t1=ttt
       { $val = ($t0.val == Type.NUM && $t1.val == Type.NUM) ? Type.NUM : (($t0.val == Type.STR && $t1.val == Type.STR) ? Type.STR : (($t0.val == Type.BOOL && $t1.val == Type.BOOL) ? Type.BOOL : Type.ERR)); }
     | t0=ttt EQUALS t1=ttt
        { $val = (($t0.val == Type.NUM && $t1.val == Type.NUM) || ($t0.val == Type.STR && $t1.val == Type.STR) || ($t0.val == Type.BOOL && $t1.val == Type.BOOL)) ? Type.BOOL : Type.ERR; }
     | LPAR t=ttt RPAR
       { $val = $t.val; }
     | { System.out.println("Evaluating NUMBER"); }
       TYPE
       { $val = getValue($TYPE.text); }
     ;
