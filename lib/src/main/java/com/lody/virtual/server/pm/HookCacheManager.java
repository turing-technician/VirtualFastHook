package com.lody.virtual.server.pm;

import java.util.ArrayList;
import java.util.HashMap;

public class HookCacheManager {

    public final static String HOOK_PROCESS = "fasthook.hook.process";
    public final static String HOOK_INFO = "fasthook.hook.info";

    static final HashMap<String, ArrayList<HookCacheInfo>> mHookPluginCache = new HashMap<String, ArrayList<HookCacheInfo>>();
    static final HashMap<String, HookCacheInfo> mHookPluginCachePackage = new HashMap<String, HookCacheInfo>();

    public static class HookCacheInfo {
        String mName;
        String mProcess;
        String mHookInfo;

        public HookCacheInfo(String name, String process, String hookInfo) {
            this.mName = name;
            this.mProcess = process;
            this.mHookInfo = hookInfo;
        }
    }

    public static void put(String process, HookCacheInfo info) {
        synchronized (HookCacheManager.class) {
            ArrayList<HookCacheInfo> list = mHookPluginCache.get(process);
            if(list == null) {
                list = new ArrayList<HookCacheInfo>();
            }
            list.add(info);
            mHookPluginCache.put(process,list);
            mHookPluginCachePackage.put(info.mName,info);
        }
    }

    public static void remove(String packageName) {
        synchronized (HookCacheManager.class) {
            HookCacheInfo info = mHookPluginCachePackage.remove(packageName);
            if(info != null) {
                ArrayList<HookCacheInfo> list = mHookPluginCache.get(info.mProcess);
                for(int i = list.size() - 1; i >= 0; i--) {
                    HookCacheInfo info2 = list.get(i);
                    if(info2.mName == info.mName) {
                        list.remove(i);
                    }
                }
                if(list.isEmpty()) {
                    mHookPluginCache.remove(info.mProcess);
                }
            }
        }
    }

    public static String[] get(String process) {
        synchronized (HookCacheManager.class) {
            ArrayList<HookCacheInfo> list = mHookPluginCache.get(process);
            if(list != null) {
                String[] result = new String[list.size()];
                int index = 0;
                for(HookCacheInfo info : list) {
                    StringBuilder item = new StringBuilder();
                    item.append((char)(info.mName.length()));
                    item.append(info.mName);
                    item.append(info.mHookInfo);
                    result[index] = item.toString();
                    index++;
                }
                return result;
            }
            return null;
        }
    }
}
