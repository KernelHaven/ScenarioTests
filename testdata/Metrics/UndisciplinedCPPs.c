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