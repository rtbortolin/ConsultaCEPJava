package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import main.java.consultaCEP.domain.entities.Search;
import main.java.consultaCEP.infra.db.SearchRepository;
import main.java.consultaCEP.interfaces.ISearchRepository;

import org.junit.Before;
import org.junit.Test;

public class SearchRepositoryTest {

	private ISearchRepository repository;

	@Before
	public void setUp() {
		SearchRepository repo = new SearchRepository();
		repo.connection.dropDataBase();
		repository = repo;
	}

	@Test
	public void insert_search_should_insert_a_search_in_db() {
		String cep = "13570-003";
		Search search = new Search(cep);
	
		search = repository.insert(search);
		
		assertNotNull(search.getId());
	}
	

	@Test
	public void list_should_return_a_list_with_all_searches_in_db() {
		String cep = "14403-720";
		Search search = new Search(cep);
		
		repository.insert(search);
		
		List<Search> searches = repository.list();

		assertNotNull(searches);
		assertEquals(1, searches.size());
	}
}
