package org.kostiskag.unitynetwork.common.pairs;

public class Quartet<A, B, C, D> {
    A val1;
    B val2;
    C val3;
    D val4;

    public Quartet(A val1, B val2, C val3, D val4) {
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
        this.val4 = val4;
    }

    public A getVal1() {
        return val1;
    }

    public B getVal2() {
        return val2;
    }

    public C getVal3() {
        return val3;
    }

    public D getVal4() {
        return val4;
    }
}
