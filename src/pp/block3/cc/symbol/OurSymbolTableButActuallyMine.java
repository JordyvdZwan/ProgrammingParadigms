package pp.block3.cc.symbol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jordy van der Zwan on 18-May-17.
 */
public class OurSymbolTableButActuallyMine implements SymbolTable{

    private int scopeDepth = 0;
    private Map<Integer, Set<String>> table = new HashMap<>();

    public OurSymbolTableButActuallyMine() {
        table.put(0, new HashSet<>());
    }

    /** Adds a next deeper scope level. */
    @Override
    public void openScope() {
        table.put(++scopeDepth, new HashSet<>());
    }

    /** Removes the deepest scope level.
     * @throws RuntimeException if the table only contains the outer scope.
     */
    @Override
    public void closeScope() {
        if (scopeDepth <= 0) throw new RuntimeException();
        table.remove(scopeDepth--);
    }

    /** Tries to declare a given identifier in the deepest scope level.
     * @return <code>true</code> if the identifier was added,
     * <code>false</code> if it was already declared in this scope.
     */
    @Override
    public boolean add(String id) {
        if (table.get(scopeDepth).contains(id)) return false;
        return table.get(scopeDepth).add(id);
    }

    /** Tests if a given identifier is in the scope of any declaration.
     * @return <code>true</code> if there is any enclosing scope in which
     * the identifier is declared; <code>false</code> otherwise.
     */
    @Override
    public boolean contains(String id) {
        for (Integer key : table.keySet()) {
            if (table.get(key).contains(id)) return true;
        }
        return false;
    }
}
