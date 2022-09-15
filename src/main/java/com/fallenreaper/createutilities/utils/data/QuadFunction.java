package com.fallenreaper.createutilities.utils.data;

@SuppressWarnings("all")
public interface QuadFunction<T, U, V, F, R>  {
    T apply(U u, V v, F f, R r);

}
