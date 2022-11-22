package com.rmf.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.rmf.models.entity.Cliente;

@Repository
public interface IClienteRepository extends PagingAndSortingRepository<Cliente, Long>, CrudRepository<Cliente, Long>{

}
