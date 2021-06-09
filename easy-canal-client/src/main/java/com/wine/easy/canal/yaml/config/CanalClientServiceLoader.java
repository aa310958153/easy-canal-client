package com.wine.easy.canal.yaml.config;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.yaml.config
 * @ClassName CanalClientServiceLoader
 * @Author qiang.li
 * @Date 2021/5/31 9:36 上午
 * @Description TODO
 */
public class CanalClientServiceLoader {
    private static final Map<Class<?>, Collection<Class<?>>> SERVICES = new ConcurrentHashMap<>();

    /**
     * Register service.
     *
     * @param service service class
     * @param <T> type of service
     */
    public static <T> void register(final Class<T> service) {
        if (SERVICES.containsKey(service)) {
            return;
        }
        for (T each : ServiceLoader.load(service)) {
            registerServiceClass(service, each);
        }
    }

    private static <T> void registerServiceClass(final Class<T> service, final T instance) {
        Collection<Class<?>> serviceClasses = SERVICES.computeIfAbsent(service, unused -> new LinkedHashSet<>());
        serviceClasses.add(instance.getClass());
    }

    /**
     * New service instances.
     *
     * @param service service class
     * @param <T> type of service
     * @return service instances
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> newServiceInstances(final Class<T> service) {
        return SERVICES.containsKey(service) ? SERVICES.get(service).stream().map(each -> (T) newServiceInstance(each)).collect(Collectors.toList()) : Collections.emptyList();
    }

    private static Object newServiceInstance(final Class<?> clazz) {
//        try {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
//        } catch (final InstantiationException | IllegalAccessException ex) {
//            throw new ServiceLoaderInstantiationException(clazz, ex);
//        }
        return null;
    }
}
