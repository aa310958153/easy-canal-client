package com.wine.easy.canal.reflector;

import com.wine.easy.canal.exception.ReflectionException;

import java.lang.reflect.*;
import java.util.*;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.reflector
 * @ClassName d
 * @Author qiang.li
 * @Date 2021/3/24 3:29 下午
 * @Description TODO
 */
public class Reflector {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private Class<?> type;
    private String[] readablePropertyNames;
    private String[] writeablePropertyNames;
    private Map<String, Invoker> setMethods;
    private Map<String, Invoker> getMethods;
    private Map<String, Class<?>> setTypes;
    private Map<String, Class<?>> getTypes;
    private Constructor<?> defaultConstructor;
    private Map<String, String> caseInsensitivePropertyMap;

    /**
     * 初始化并将对应的元数据信息封装起来
     * @param clazz
     */
    public Reflector(Class<?> clazz) throws ReflectionException {
        this.readablePropertyNames = EMPTY_STRING_ARRAY;
        this.writeablePropertyNames = EMPTY_STRING_ARRAY;
        //初始化几个map
        this.setMethods = new HashMap();
        this.getMethods = new HashMap();
        this.setTypes = new HashMap();
        this.getTypes = new HashMap();
        this.caseInsensitivePropertyMap = new HashMap();
        this.type = clazz;
        //反射查找默认构造函数到defaultConstructor
        this.addDefaultConstructor(clazz);
        //反射获得所有的get方法元数据保存到以Invoker保存getMethods
        this.addGetMethods(clazz);
        //反射获得所有的set方法元数据以invokersetMethods
        this.addSetMethods(clazz);
        //反射获得所有的Fields元数据
        this.addFields(clazz);
        this.readablePropertyNames = (String[])this.getMethods.keySet().toArray(new String[this.getMethods.keySet().size()]);
        this.writeablePropertyNames = (String[])this.setMethods.keySet().toArray(new String[this.setMethods.keySet().size()]);
        String[] arr$ = this.readablePropertyNames;
        int len$ = arr$.length;

        int i$;
        String propName;
        for(i$ = 0; i$ < len$; ++i$) {
            propName = arr$[i$];
            this.caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
        }

        arr$ = this.writeablePropertyNames;
        len$ = arr$.length;

        for(i$ = 0; i$ < len$; ++i$) {
            propName = arr$[i$];
            this.caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
        }

    }

    private void addDefaultConstructor(Class<?> clazz) {
        Constructor<?>[] consts = clazz.getDeclaredConstructors();
        Constructor[] arr$ = consts;
        int len$ = consts.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Constructor<?> constructor = arr$[i$];
            if (constructor.getParameterTypes().length == 0) {
                if (canAccessPrivateMethods()) {
                    try {
                        constructor.setAccessible(true);
                    } catch (Exception var8) {
                        ;
                    }
                }

                if (constructor.isAccessible()) {
                    this.defaultConstructor = constructor;
                }
            }
        }

    }

    private void addGetMethods(Class<?> cls) throws ReflectionException {
        Map<String, List<Method>> conflictingGetters = new HashMap();
        Method[] methods = this.getClassMethods(cls);
        Method[] arr$ = methods;
        int len$ = methods.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Method method = arr$[i$];
            String name = method.getName();
            if (name.startsWith("get") && name.length() > 3) {
                if (method.getParameterTypes().length == 0) {
                    name = PropertyNamer.methodToProperty(name);
                    this.addMethodConflict(conflictingGetters, name, method);
                }
            } else if (name.startsWith("is") && name.length() > 2 && method.getParameterTypes().length == 0) {
                name = PropertyNamer.methodToProperty(name);
                this.addMethodConflict(conflictingGetters, name, method);
            }
        }

        this.resolveGetterConflicts(conflictingGetters);
    }

    private void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters) throws ReflectionException {
        Iterator i$ = conflictingGetters.keySet().iterator();

        while(true) {
            while(i$.hasNext()) {
                String propName = (String)i$.next();
                List<Method> getters = (List)conflictingGetters.get(propName);
                Iterator<Method> iterator = getters.iterator();
                Method firstMethod = (Method)iterator.next();
                if (getters.size() == 1) {
                    this.addGetMethod(propName, firstMethod);
                } else {
                    Method getter = firstMethod;
                    Class getterType = firstMethod.getReturnType();

                    while(iterator.hasNext()) {
                        Method method = (Method)iterator.next();
                        Class<?> methodType = method.getReturnType();
                        if (methodType.equals(getterType)) {
                            throw new ReflectionException("Illegal overloaded getter method with ambiguous type for property " + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " + "specification and can cause unpredicatble results.");
                        }

                        if (!methodType.isAssignableFrom(getterType)) {
                            if (!getterType.isAssignableFrom(methodType)) {
                                throw new ReflectionException("Illegal overloaded getter method with ambiguous type for property " + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " + "specification and can cause unpredicatble results.");
                            }

                            getter = method;
                            getterType = methodType;
                        }
                    }

                    this.addGetMethod(propName, getter);
                }
            }

            return;
        }
    }

    private void addGetMethod(String name, Method method) {
        if (this.isValidPropertyName(name)) {
            this.getMethods.put(name, new MethodInvoker(method));
            Type returnType = TypeParameterResolver.resolveReturnType(method, this.type);
            this.getTypes.put(name, this.typeToClass(returnType));
        }

    }

    private void addSetMethods(Class<?> cls) throws ReflectionException {
        Map<String, List<Method>> conflictingSetters = new HashMap();
        Method[] methods = this.getClassMethods(cls);
        Method[] arr$ = methods;
        int len$ = methods.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Method method = arr$[i$];
            String name = method.getName();
            if (name.startsWith("set") && name.length() > 3 && method.getParameterTypes().length == 1) {
                name = PropertyNamer.methodToProperty(name);
                this.addMethodConflict(conflictingSetters, name, method);
            }
        }

        this.resolveSetterConflicts(conflictingSetters);
    }

    private void addMethodConflict(Map<String, List<Method>> conflictingMethods, String name, Method method) {
        List<Method> list = (List)conflictingMethods.get(name);
        if (list == null) {
            list = new ArrayList();
            conflictingMethods.put(name, list);
        }

        ((List)list).add(method);
    }

    private void resolveSetterConflicts(Map<String, List<Method>> conflictingSetters) throws ReflectionException {
        Iterator i$ = conflictingSetters.keySet().iterator();

        while(true) {
            while(i$.hasNext()) {
                String propName = (String)i$.next();
                List<Method> setters = (List)conflictingSetters.get(propName);
                Method firstMethod = (Method)setters.get(0);
                if (setters.size() == 1) {
                    this.addSetMethod(propName, firstMethod);
                } else {
                    Class<?> expectedType = (Class)this.getTypes.get(propName);
                    if (expectedType == null) {
                        throw new ReflectionException("Illegal overloaded setter method with ambiguous type for property " + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " + "specification and can cause unpredicatble results.");
                    }

                    Iterator<Method> methods = setters.iterator();
                    Method setter = null;

                    while(methods.hasNext()) {
                        Method method = (Method)methods.next();
                        if (method.getParameterTypes().length == 1 && expectedType.equals(method.getParameterTypes()[0])) {
                            setter = method;
                            break;
                        }
                    }

                    if (setter == null) {
                        throw new ReflectionException("Illegal overloaded setter method with ambiguous type for property " + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " + "specification and can cause unpredicatble results.");
                    }

                    this.addSetMethod(propName, setter);
                }
            }

            return;
        }
    }

    private void addSetMethod(String name, Method method) {
        if (this.isValidPropertyName(name)) {
            this.setMethods.put(name, new MethodInvoker(method));
            Type[] paramTypes = TypeParameterResolver.resolveParamTypes(method, this.type);
            this.setTypes.put(name, this.typeToClass(paramTypes[0]));
        }

    }

    private Class<?> typeToClass(Type src) {
        Class<?> result = null;
        if (src instanceof Class) {
            result = (Class)src;
        } else if (src instanceof ParameterizedType) {
            result = (Class)((ParameterizedType)src).getRawType();
        } else if (src instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType)src).getGenericComponentType();
            if (componentType instanceof Class) {
                result = Array.newInstance((Class)componentType, 0).getClass();
            } else {
                Class<?> componentClass = this.typeToClass(componentType);
                result = Array.newInstance(componentClass, 0).getClass();
            }
        }

        if (result == null) {
            result = Object.class;
        }

        return result;
    }

    private void addFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Field[] arr$ = fields;
        int len$ = fields.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Field field = arr$[i$];
            if (canAccessPrivateMethods()) {
                try {
                    field.setAccessible(true);
                } catch (Exception var8) {
                    ;
                }
            }

            if (field.isAccessible()) {
                if (!this.setMethods.containsKey(field.getName())) {
                    int modifiers = field.getModifiers();
                    if (!Modifier.isFinal(modifiers) || !Modifier.isStatic(modifiers)) {
                        this.addSetField(field);
                    }
                }

                if (!this.getMethods.containsKey(field.getName())) {
                    this.addGetField(field);
                }
            }
        }

        if (clazz.getSuperclass() != null) {
            this.addFields(clazz.getSuperclass());
        }

    }

    private void addSetField(Field field) {
        if (this.isValidPropertyName(field.getName())) {
            this.setMethods.put(field.getName(), new SetFieldInvoker(field));
            Type fieldType = TypeParameterResolver.resolveFieldType(field, this.type);
            this.setTypes.put(field.getName(), this.typeToClass(fieldType));
        }

    }

    private void addGetField(Field field) {
        if (this.isValidPropertyName(field.getName())) {
            this.getMethods.put(field.getName(), new GetFieldInvoker(field));
            Type fieldType = TypeParameterResolver.resolveFieldType(field, this.type);
            this.getTypes.put(field.getName(), this.typeToClass(fieldType));
        }

    }

    private boolean isValidPropertyName(String name) {
        return !name.startsWith("$") && !"serialVersionUID".equals(name) && !"class".equals(name);
    }

    private Method[] getClassMethods(Class<?> cls) {
        Map<String, Method> uniqueMethods = new HashMap();

        for(Class currentClass = cls; currentClass != null; currentClass = currentClass.getSuperclass()) {
            this.addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());
            Class<?>[] interfaces = currentClass.getInterfaces();
            Class[] arr$ = interfaces;
            int len$ = interfaces.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Class<?> anInterface = arr$[i$];
                this.addUniqueMethods(uniqueMethods, anInterface.getMethods());
            }
        }

        Collection<Method> methods = uniqueMethods.values();
        return (Method[])methods.toArray(new Method[methods.size()]);
    }

    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        Method[] arr$ = methods;
        int len$ = methods.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Method currentMethod = arr$[i$];
            if (!currentMethod.isBridge()) {
                String signature = this.getSignature(currentMethod);
                if (!uniqueMethods.containsKey(signature)) {
                    if (canAccessPrivateMethods()) {
                        try {
                            currentMethod.setAccessible(true);
                        } catch (Exception var9) {
                            ;
                        }
                    }

                    uniqueMethods.put(signature, currentMethod);
                }
            }
        }

    }

    private String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            sb.append(returnType.getName()).append('#');
        }

        sb.append(method.getName());
        Class<?>[] parameters = method.getParameterTypes();

        for(int i = 0; i < parameters.length; ++i) {
            if (i == 0) {
                sb.append(':');
            } else {
                sb.append(',');
            }

            sb.append(parameters[i].getName());
        }

        return sb.toString();
    }

    private static boolean canAccessPrivateMethods() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }

            return true;
        } catch (SecurityException var1) {
            return false;
        }
    }

    public Class<?> getType() {
        return this.type;
    }

    public Constructor<?> getDefaultConstructor() throws ReflectionException {
        if (this.defaultConstructor != null) {
            return this.defaultConstructor;
        } else {
            throw new ReflectionException("There is no default constructor for " + this.type);
        }
    }

    public boolean hasDefaultConstructor() {
        return this.defaultConstructor != null;
    }

    public Invoker getSetInvoker(String propertyName) throws ReflectionException {
        Invoker method = (Invoker)this.setMethods.get(propertyName);
        if (method == null) {
            throw new ReflectionException("There is no setter for property named '" + propertyName + "' in '" + this.type + "'");
        } else {
            return method;
        }
    }

    public Invoker getGetInvoker(String propertyName) throws ReflectionException {
        Invoker method = (Invoker)this.getMethods.get(propertyName);
        if (method == null) {
            throw new ReflectionException("There is no getter for property named '" + propertyName + "' in '" + this.type + "'");
        } else {
            return method;
        }
    }

    public Class<?> getSetterType(String propertyName) throws ReflectionException {
        Class<?> clazz = (Class)this.setTypes.get(propertyName);
        if (clazz == null) {
            throw new ReflectionException("There is no setter for property named '" + propertyName + "' in '" + this.type + "'");
        } else {
            return clazz;
        }
    }

    public Class<?> getGetterType(String propertyName) throws ReflectionException {
        Class<?> clazz = (Class)this.getTypes.get(propertyName);
        if (clazz == null) {
            throw new ReflectionException("There is no getter for property named '" + propertyName + "' in '" + this.type + "'");
        } else {
            return clazz;
        }
    }

    public String[] getGetablePropertyNames() {
        return this.readablePropertyNames;
    }

    public String[] getSetablePropertyNames() {
        return this.writeablePropertyNames;
    }

    public boolean hasSetter(String propertyName) {
        return this.setMethods.keySet().contains(propertyName);
    }

    public boolean hasGetter(String propertyName) {
        return this.getMethods.keySet().contains(propertyName);
    }

    public String findPropertyName(String name) {
        return (String)this.caseInsensitivePropertyMap.get(name.toUpperCase(Locale.ENGLISH));
    }
}