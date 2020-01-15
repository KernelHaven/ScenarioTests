void func_NoVariability() {
    int declaration;
}

void func_Comment_NoVariability() {
    // Static comment
    int declaration;
}

void func_Comment_Variability() {
    #ifdef A
    // Variable comment
    int declaration;
    #endif
}

void func_Comment_outside_Variability() {
    // Static comment
    #ifdef A
    int declaration;
    #endif
}

void func_Comment_SameLine() {
    int declaration; // A coment in same line
}
void func_Comment_nested_Variability() {
    #ifdef A
    int declaration;
    
    #ifdef B
    // A nested comment
    int declaration2;
    #endif
    
    #endif
}

void func_Comment_multiline_NoVariability() {
    /*
     * Comment
     * spans
     * multiple
     * lines.
     */
    int declaration;
}

void func_Comment_multiple_NoVariability() {
    // Comment
    // Further details
    int declaration;
}

void func_Comment_nested_NoVariability() {
    while (1) {
        // Loop's comment
        ;
    }
}

void func_Comment_Mixed_Variability() {
    int declaration;
    
    #ifdef B
    // A nested comment
    int declaration2;
    #endif
}