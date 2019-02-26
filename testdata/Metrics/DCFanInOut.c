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