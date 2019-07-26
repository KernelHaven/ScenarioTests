void condLoopSplitedInOnePart() {
    #ifdef A
        while(1)
    #endif
    {
        ;
    }
}

void condLoopSplitedInTwoParts() {
    #ifdef A
        while(1) {
    #endif
        ;
    #ifdef A
    }
    #endif
}

void condIfSplitedInOnePart() {
    #ifdef A
        if (true) 
    #endif
    {
        ;
    }
}

void condIfExpression() {
    int a;
    int b;
    
    if (a > 1 
    #ifdef A 
        && b > 1 
    #endif
    ) {
        ;
    }
}

void condTypeOfDeclaration() {
    #ifdef A
        int
    #else
        float
    #endif
        variable;
}


// Examples from Discipline Matters: Refactoring of Preprocessor Directives in the #ifdef Hell
void ref1_bad() {
    return intA
    #ifdef EXP
        + intB
    #else
        + intC
    #endif
    ;
}

void ref1_good() {
    int intA;
    #ifdef EXP
        return intA + intB;
    #else
        return intA + intC;
    #endif
}

void ref2_bad() {
    if (cond1
    #ifdef EXP
        + cond2
    #endif
    ) {
        ;
    }
}

void ref2_good() {
    int cond1;
    #ifdef EXP
    cond1 = cond1 + cond2;
    #endif
    if (cond1) {
        ;
    }
}

void ref4_bad() {
    #ifdef EXP
    if (cond1)
    #endif
    {
        ;
    }
}

void ref4_good() {
    int test = 1;
    #ifdef EXP
    test = cond1;
    #endif
    if (test) {
        ;
    }
}

void ref5_bad() {
    #ifdef EXP
    if (cond1) {
        ;
    } else
    #endif
    {
        ;
    }
}

void ref5_good() {
    int test = 1;
    #ifdef EXP
    if (cond1) {
        ;
        test = 0;
    }
    #endif
    if (test) {
        ;
    }
}

void ref6_bad(
    #ifdef EXP
    int paramA
    #endif
) {}

void ref6_good(PARAM) {}