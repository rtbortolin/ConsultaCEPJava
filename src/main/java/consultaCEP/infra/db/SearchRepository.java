package main.java.consultaCEP.infra.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import main.java.consultaCEP.domain.entities.Search;
import main.java.consultaCEP.interfaces.ISearchRepository;

public class SearchRepository extends MongoConnection implements
		ISearchRepository {

	public SearchRepository() {
		super("searches");
	}

	@Override
	public Search insert(Search search) {
		try {
			openConnection();
			DBObject obj = convert(search);
			collection.insert(obj);
			return fill((BasicDBObject) obj);
		} finally {
			closeConnection();
		}
	}

	@Override
	public List<Search> list() {
		List<Search> searches = new ArrayList<Search>();

		openConnection();
		DBCursor cursor = collection.find();
		try {
			while (cursor.hasNext()) {
				searches.add(fill((BasicDBObject) cursor.next()));
			}
		} finally {
			closeConnection();
		}
		return searches;
	}

	private Search fill(BasicDBObject dbObject) {
		ObjectId id = (ObjectId) dbObject.get("_id");
		Date createdIn = dbObject.getDate("created-in");
		String cep = dbObject.getString("cep");

		return new Search(id, createdIn, cep);
	}

	private DBObject convert(Search search) {
		BasicDBObject dbo = new BasicDBObject();

		dbo.put("cep", search.getCep());
		dbo.put("created-in", search.getCreatedIn());

		return dbo;
	}

}
