package br.com.erudio.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.data.dto.v1.PersonDTO;
import br.com.erudio.data.dto.v2.PersonDTOV2;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.mapper.custom.PersonMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;

@Service
public class PersonService {

	private Logger logger = Logger.getLogger(PersonService.class.getName());
	
	@Autowired 
	private PersonRepository repository;
	
	@Autowired 
	private PersonMapper mapper;
	
	public List<PersonDTO> findAll(){
		logger.info("Finding all people!");
		return DozerMapper.parseListObjects(repository.findAll(), PersonDTO.class);
	}
	
	public PersonDTO findById(Long id) {
		
		logger.info("Finding one person!");
		
		return DozerMapper.parseObject(repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!")), PersonDTO.class);
		
//		return repository.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
	}
	
	public PersonDTO create(PersonDTO person) {
		logger.info("Create one person!");
		
		Person entity = DozerMapper.parseObject(person, Person.class);
		
		return DozerMapper.parseObject(repository.save(entity), PersonDTO.class);
	}
	
	public PersonDTOV2 createV2(PersonDTOV2 personDTOV2) {
		logger.info("Create one person with V2!");
		Person entity = mapper.convertDtoToEntity(personDTOV2);
		
		return mapper.convertEntityToDto(repository.save(entity));
	}
	
	public PersonDTO update(PersonDTO person) {
		logger.info("Update one person!");
		
		Person entity = repository.findById(person.getId())
			.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		return DozerMapper.parseObject(repository.save(entity), PersonDTO.class);
	}

	public void delete(Long id) {
		logger.info("Deleting one person!");
		
		PersonDTO entity = DozerMapper.parseObject(repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!")), PersonDTO.class) ;
		
		repository.delete(DozerMapper.parseObject(entity, Person.class));
		
	}

}
