package main.java.consultaCEP.interfaces;

import java.util.List;

import main.java.consultaCEP.domain.entities.Search;

public interface ISearchRepository {
		public Search insert(Search search);
		public List<Search> list();
}
