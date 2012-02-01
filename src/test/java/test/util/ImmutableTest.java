package test.util;

import static com.soletta.seek.util.None.none;
import static com.soletta.seek.util.Some.some;

import org.junit.Test;

import com.soletta.seek.util.Maybe;
import com.soletta.seek.util.None;
import com.soletta.seek.util.Some;

public class ImmutableTest {

    static int nx;
    
    class X {
        int id = nx++;

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("X [id=");
            builder.append(id);
            builder.append("]");
            return builder.toString();
        }
        
    }
    
    @Test
    public void buildSome() {
    
        Maybe<X> mx = some(new X()), nomx = none();

        System.out.println(mx);
        System.out.println(nomx);
        
        
    }
}
