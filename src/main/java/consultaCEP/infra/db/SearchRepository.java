package main.java.consultaCEP.infra.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCursor;

import main.java.consultaCEP.domain.entities.Search;
import main.java.consultaCEP.interfaces.ISearchRepository;

public class SearchRepository extends BaseRepository implements ISearchRepository {

	public SearchRepository() {
		super("searches");
	}

	@Override
	public Search insert(Search search) {
		try {
			connection.openConnection();
			Document obj = convert(search);
			connection.getCollection().insertOne(obj);
			return fill(obj);
		} finally {
			connection.closeConnection();
		}
	}

	@Override
	public List<Search> list() {
		List<Search> searches = new ArrayList<Search>();

		connection.openConnection();
		MongoCursor<Document> cursor = connection.getCollection().find().iterator();
		try {
			while (cursor.hasNext()) {
				searches.add(fill(cursor.next()));
			}
		} finally {
			connection.closeConnection();
		}

		return searches;
	}

	private Search fill(Document dbObject) {
		ObjectId id = (ObjectId) dbObject.get("_id");
		Date createdIn = dbObject.getDate("created-in");
		String cep = dbObject.getString("cep");

		return new Search(id, createdIn, cep);
	}

	private Document convert(Search search) {
		Document dbo = new Document();

		dbo.append("cep", search.getCep());
		dbo.append("created-in", search.getCreatedIn());

		return dbo;
	}

}
