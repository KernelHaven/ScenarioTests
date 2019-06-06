#ifdef A
void func1() {
	// real implementation
}
#endif

void func2() {
#ifdef A
	func1();
#endif
}
