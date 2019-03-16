//
// VirtualApp Native Project
//

#ifndef NDK_HOOK_H
#define NDK_HOOK_H


#include <string>
#include <list>
#include <jni.h>
#include <dlfcn.h>
#include <stddef.h>
#include <fcntl.h>
#include <dirent.h>
#include <unistd.h>
#include <sys/syscall.h>

#include "Jni/Helper.h"

#define ANDROID_O 26
#define ANDROID_N 24
#define ANDROID_M 23
#define ANDROID_L 21

#define HOOK_SYMBOL(handle, func) hook_function(handle, #func, (void*) new_##func, (void**) &orig_##func)
#define HOOK_DEF(ret, func, ...) \
  static ret (*orig_##func)(__VA_ARGS__); \
  static ret new_##func(__VA_ARGS__)


namespace IOUniformer {
    void hookExec(int api_level);

    void init_env_before_all();

    void startUniformer(const char *so_path, int api_level, int preview_api_level);

    void redirect(const char *orig_path, const char *new_path);

    void whitelist(const char *path);

    const char *query(const char *orig_path);

    const char *reverse(const char *redirected_path);

    void forbid(const char *path);
}

#endif //NDK_HOOK_H
