package com.wine.easy.canal.reflector;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.reflector
 * @ClassName d
 * @Author qiang.li
 * @Date 2021/3/24 3:38 下午
 * @Description TODO
 */

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

public class TypeParameterResolver {
    public static Type resolveFieldType(Field field, Type srcType) {
        Type fieldType = field.getGenericType();
        Class<?> declaringClass = field.getDeclaringClass();
        return resolveType(fieldType, srcType, declaringClass);
    }

    public static Type resolveReturnType(Method method, Type srcType) {
        Type returnType = method.getGenericReturnType();
        Class<?> declaringClass = method.getDeclaringClass();
        return resolveType(returnType, srcType, declaringClass);
    }

    public static Type[] resolveParamTypes(Method method, Type srcType) {
        Type[] paramTypes = method.getGenericParameterTypes();
        Class<?> declaringClass = method.getDeclaringClass();
        Type[] result = new Type[paramTypes.length];

        for(int i = 0; i < paramTypes.length; ++i) {
            result[i] = resolveType(paramTypes[i], srcType, declaringClass);
        }

        return result;
    }

    private static Type resolveType(Type type, Type srcType, Class<?> declaringClass) {
        if (type instanceof TypeVariable) {
            return resolveTypeVar((TypeVariable)type, srcType, declaringClass);
        } else if (type instanceof ParameterizedType) {
            return resolveParameterizedType((ParameterizedType)type, srcType, declaringClass);
        } else {
            return type instanceof GenericArrayType ? resolveGenericArrayType((GenericArrayType)type, srcType, declaringClass) : type;
        }
    }

    private static Type resolveGenericArrayType(GenericArrayType genericArrayType, Type srcType, Class<?> declaringClass) {
        Type componentType = genericArrayType.getGenericComponentType();
        Type resolvedComponentType = null;
        if (componentType instanceof TypeVariable) {
            resolvedComponentType = resolveTypeVar((TypeVariable)componentType, srcType, declaringClass);
        } else if (componentType instanceof GenericArrayType) {
            resolvedComponentType = resolveGenericArrayType((GenericArrayType)componentType, srcType, declaringClass);
        } else if (componentType instanceof ParameterizedType) {
            resolvedComponentType = resolveParameterizedType((ParameterizedType)componentType, srcType, declaringClass);
        }

        return (Type)(resolvedComponentType instanceof Class ? Array.newInstance((Class)resolvedComponentType, 0).getClass() : new TypeParameterResolver.GenericArrayTypeImpl((Type)resolvedComponentType));
    }

    private static ParameterizedType resolveParameterizedType(ParameterizedType parameterizedType, Type srcType, Class<?> declaringClass) {
        Class<?> rawType = (Class)parameterizedType.getRawType();
        Type[] typeArgs = parameterizedType.getActualTypeArguments();
        Type[] args = new Type[typeArgs.length];

        for(int i = 0; i < typeArgs.length; ++i) {
            if (typeArgs[i] instanceof TypeVariable) {
                args[i] = resolveTypeVar((TypeVariable)typeArgs[i], srcType, declaringClass);
            } else if (typeArgs[i] instanceof ParameterizedType) {
                args[i] = resolveParameterizedType((ParameterizedType)typeArgs[i], srcType, declaringClass);
            } else if (typeArgs[i] instanceof WildcardType) {
                args[i] = resolveWildcardType((WildcardType)typeArgs[i], srcType, declaringClass);
            } else {
                args[i] = typeArgs[i];
            }
        }

        return new TypeParameterResolver.ParameterizedTypeImpl(rawType, (Type)null, args);
    }

    private static Type resolveWildcardType(WildcardType wildcardType, Type srcType, Class<?> declaringClass) {
        Type[] lowerBounds = resolveWildcardTypeBounds(wildcardType.getLowerBounds(), srcType, declaringClass);
        Type[] upperBounds = resolveWildcardTypeBounds(wildcardType.getUpperBounds(), srcType, declaringClass);
        return new TypeParameterResolver.WildcardTypeImpl(lowerBounds, upperBounds);
    }

    private static Type[] resolveWildcardTypeBounds(Type[] bounds, Type srcType, Class<?> declaringClass) {
        Type[] result = new Type[bounds.length];

        for(int i = 0; i < bounds.length; ++i) {
            if (bounds[i] instanceof TypeVariable) {
                result[i] = resolveTypeVar((TypeVariable)bounds[i], srcType, declaringClass);
            } else if (bounds[i] instanceof ParameterizedType) {
                result[i] = resolveParameterizedType((ParameterizedType)bounds[i], srcType, declaringClass);
            } else if (bounds[i] instanceof WildcardType) {
                result[i] = resolveWildcardType((WildcardType)bounds[i], srcType, declaringClass);
            } else {
                result[i] = bounds[i];
            }
        }

        return result;
    }

    private static Type resolveTypeVar(TypeVariable<?> typeVar, Type srcType, Class<?> declaringClass) {
        Class clazz;
        if (srcType instanceof Class) {
            clazz = (Class)srcType;
        } else {
            if (!(srcType instanceof ParameterizedType)) {
                throw new IllegalArgumentException("The 2nd arg must be Class or ParameterizedType, but was: " + srcType.getClass());
            }

            ParameterizedType parameterizedType = (ParameterizedType)srcType;
            clazz = (Class)parameterizedType.getRawType();
        }

        if (clazz == declaringClass) {
            Type[] bounds = typeVar.getBounds();
            return (Type)(bounds.length > 0 ? bounds[0] : Object.class);
        } else {
            Type superclass = clazz.getGenericSuperclass();
            Type result = scanSuperTypes(typeVar, srcType, declaringClass, clazz, superclass);
            if (result != null) {
                return result;
            } else {
                Type[] superInterfaces = clazz.getGenericInterfaces();
                Type[] var7 = superInterfaces;
                int var8 = superInterfaces.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    Type superInterface = var7[var9];
                    result = scanSuperTypes(typeVar, srcType, declaringClass, clazz, superInterface);
                    if (result != null) {
                        return result;
                    }
                }

                return Object.class;
            }
        }
    }

    private static Type scanSuperTypes(TypeVariable<?> typeVar, Type srcType, Class<?> declaringClass, Class<?> clazz, Type superclass) {
        if (superclass instanceof ParameterizedType) {
            ParameterizedType parentAsType = (ParameterizedType)superclass;
            Class<?> parentAsClass = (Class)parentAsType.getRawType();
            TypeVariable<?>[] parentTypeVars = parentAsClass.getTypeParameters();
            if (srcType instanceof ParameterizedType) {
                parentAsType = translateParentTypeVars((ParameterizedType)srcType, clazz, parentAsType);
            }

            if (declaringClass == parentAsClass) {
                for(int i = 0; i < parentTypeVars.length; ++i) {
                    if (typeVar == parentTypeVars[i]) {
                        return parentAsType.getActualTypeArguments()[i];
                    }
                }
            }

            if (declaringClass.isAssignableFrom(parentAsClass)) {
                return resolveTypeVar(typeVar, parentAsType, declaringClass);
            }
        } else if (superclass instanceof Class && declaringClass.isAssignableFrom((Class)superclass)) {
            return resolveTypeVar(typeVar, superclass, declaringClass);
        }

        return null;
    }

    private static ParameterizedType translateParentTypeVars(ParameterizedType srcType, Class<?> srcClass, ParameterizedType parentType) {
        Type[] parentTypeArgs = parentType.getActualTypeArguments();
        Type[] srcTypeArgs = srcType.getActualTypeArguments();
        TypeVariable<?>[] srcTypeVars = srcClass.getTypeParameters();
        Type[] newParentArgs = new Type[parentTypeArgs.length];
        boolean noChange = true;

        for(int i = 0; i < parentTypeArgs.length; ++i) {
            if (parentTypeArgs[i] instanceof TypeVariable) {
                for(int j = 0; j < srcTypeVars.length; ++j) {
                    if (srcTypeVars[j] == parentTypeArgs[i]) {
                        noChange = false;
                        newParentArgs[i] = srcTypeArgs[j];
                    }
                }
            } else {
                newParentArgs[i] = parentTypeArgs[i];
            }
        }

        return (ParameterizedType)(noChange ? parentType : new TypeParameterResolver.ParameterizedTypeImpl((Class)parentType.getRawType(), (Type)null, newParentArgs));
    }

    private TypeParameterResolver() {
    }

    static class GenericArrayTypeImpl implements GenericArrayType {
        private Type genericComponentType;

        GenericArrayTypeImpl(Type genericComponentType) {
            this.genericComponentType = genericComponentType;
        }

        public Type getGenericComponentType() {
            return this.genericComponentType;
        }
    }

    static class WildcardTypeImpl implements WildcardType {
        private Type[] lowerBounds;
        private Type[] upperBounds;

        WildcardTypeImpl(Type[] lowerBounds, Type[] upperBounds) {
            this.lowerBounds = lowerBounds;
            this.upperBounds = upperBounds;
        }

        public Type[] getLowerBounds() {
            return this.lowerBounds;
        }

        public Type[] getUpperBounds() {
            return this.upperBounds;
        }
    }

    static class ParameterizedTypeImpl implements ParameterizedType {
        private Class<?> rawType;
        private Type ownerType;
        private Type[] actualTypeArguments;

        public ParameterizedTypeImpl(Class<?> rawType, Type ownerType, Type[] actualTypeArguments) {
            this.rawType = rawType;
            this.ownerType = ownerType;
            this.actualTypeArguments = actualTypeArguments;
        }

        public Type[] getActualTypeArguments() {
            return this.actualTypeArguments;
        }

        public Type getOwnerType() {
            return this.ownerType;
        }

        public Type getRawType() {
            return this.rawType;
        }

        public String toString() {
            return "ParameterizedTypeImpl [rawType=" + this.rawType + ", ownerType=" + this.ownerType + ", actualTypeArguments=" + Arrays.toString(this.actualTypeArguments) + "]";
        }
    }
}
