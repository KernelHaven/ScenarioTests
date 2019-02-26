// Simple test
#ifdef A
void callingFunction() {
    #ifdef B
    calledFunction()
    #endif
}
#endif

#ifdef C
void calledFunction() {
    #ifdef D
    #endif
}
#endif

// Complex test (considering elseif conditions)
void callingFunction2() {
    #ifdef E    
        #ifdef F
        #elif defined(G) && defined(H)
        calledFunction2()
        #endif
    #endif
}

void calledFunction2() {
}