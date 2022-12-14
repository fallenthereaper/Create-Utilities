package com.fallenreaper.createutilities.core.data;

@SuppressWarnings("all")
public interface QuadFunction<T, U, V, F, R>  {
    T apply(U u, V v, F f, R r);

}
