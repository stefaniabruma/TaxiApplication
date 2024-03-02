package com.example.taximetrie.Repository;

import com.example.taximetrie.Domain.Entity;
import com.example.taximetrie.Repository.PagingUtils.Page;
import com.example.taximetrie.Repository.PagingUtils.PagingInformation;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E>{

    Page<E> findAll(PagingInformation pagingInfo);

    Page<E> findAll(PagingInformation pagingInfo, E entity);
}
