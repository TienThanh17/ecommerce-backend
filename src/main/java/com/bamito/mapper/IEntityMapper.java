package com.bamito.mapper;


import org.springframework.lang.Nullable;

import java.util.List;

public interface IEntityMapper<E, D>{
    D toDto(E e);
    List<D> toDtoList(List<E> es);
}
