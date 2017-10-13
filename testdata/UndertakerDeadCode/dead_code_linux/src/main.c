
#include <linux/kconfig.h> // for IS_ENABLED()

#if !IS_ENABLED(CONFIG_MAIN)
// this is a dead code block when considering the build model
#endif

#ifdef CONFIG_MAIN
// this is not a dead code block
#endif

#if IS_MODULE(CONFIG_MAIN)
#if IS_BUILTIN(CONFIG_MAIN)
// this is a dead code block, because CONFIG_MAIN can't be builtin and module
#endif
#endif

#ifdef CONFIG_A
// this is a dead code block when considering the build model, because A depends on MAIN=n
#endif

#ifndef CONFIG_B
#ifdef CONFIG_C
// this is a dead code block, because C depends on B
#endif
#endif

