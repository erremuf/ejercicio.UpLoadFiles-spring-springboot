package com.rmf.models.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rmf.models.entity.Cliente;
import com.rmf.repository.IClienteRepository;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteRepository clienteRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteRepository.findAll();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		clienteRepository.save(cliente);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		return clienteRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteRepository.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteRepository.findAll(pageable);
	}

	@Override
	public Optional<Cliente> findById(long l) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
}
