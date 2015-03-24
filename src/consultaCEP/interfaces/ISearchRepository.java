package consultaCEP.interfaces;

import java.util.List;

import consultaCEP.implementation.Search;

public interface ISearchRepository {
		public Search insert(Search search);
		public List<Search> list();
}
