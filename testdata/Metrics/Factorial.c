#if defined(FACTORIAL) || defined(ITERATIVE)
int factorial (int n) {
    int result = 1;
    
    #ifdef ITERATIVE
        // Iterative Variation
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
    #else
        // Recursive Variation
        if (n > 0) {
            result
                = factorial (n - 1) * n;
        }
    #endif
    
    return result;
}
#endif