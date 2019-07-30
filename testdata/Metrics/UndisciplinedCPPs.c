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
void fig1_bad() {
    if (msec > 0
    #ifdef USE_XSMP
    && xsmp_icefd != -1
    #ifdef FEAT_MZSCHEME
    || p_mzq > 0
    #endif
    #endif
    )
    gettime(&start_tv);
}

void fig1_good() {
    int time = msec > 0;
    #ifdef USE_XSMP
    time = time && xsmp_icefd != -1;
    #ifdef FEAT_MZSCHEME
    time = time || p_mzq > 0;
    #endif
    #endif
    if (time)
    gettime(&start_tv);
}

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

void ref3_bad() {
    #ifdef EXP
    if (cond1) {
    #else
    if (cond2) {
    #endif
        ;
    }
}

void ref3_good() {
    int test;
    #ifdef EXP
    test = cond1;
    #else
    test = cond2;
    #endif
    if (test) {
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
