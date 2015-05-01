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

public class SearchRepository extends BaseRepository implements
		ISearchRepository {

	public SearchRepository() {
		super("searches");
	}

	@Override
	public Search insert(Search search) {
		try {
			connection.openConnection();
			DBObject obj = convert(search);
			connection.getCollection().insert(obj);
			return fill((BasicDBObject) obj);
		} finally {
			connection.closeConnection();
		}
	}

	@Override
	public List<Search> list() {
		List<Search> searches = new ArrayList<Search>();

		connection.openConnection();
		DBCursor cursor = connection.getCollection().find();
		try {
			while (cursor.hasNext()) {
				searches.add(fill((BasicDBObject) cursor.next()));
			}
		} finally {
			connection.closeConnection();
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
