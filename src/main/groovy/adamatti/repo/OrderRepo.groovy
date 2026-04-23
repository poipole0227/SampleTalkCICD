package adamatti.repo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class OrderRepo {
	private static final String COLLECTION_NAME = "orders"

	@Autowired
	private MongoTemplate mongoTemplate

	/**
	 * List orders with optional criteria and pagination
	 */
	List list(Map mapCriteria = [:], int skip = 0, int limit = 0) {
		Criteria criteria = toCriteria(mapCriteria)
		Query query = new Query(criteria)
		
		if (skip > 0) query.skip(skip)
		if (limit > 0) query.limit(limit)
		
		mongoTemplate.find(query, Map.class, COLLECTION_NAME)
	}

	/**
	 * Get single order by ID
	 */
	Map findById(String id) {
		mongoTemplate.findById(id, Map.class, COLLECTION_NAME)
	}

	/**
	 * Count orders matching criteria
	 */
	long count(Map mapCriteria = [:]) {
		Criteria criteria = toCriteria(mapCriteria)
		Query query = new Query(criteria)
		mongoTemplate.count(query, COLLECTION_NAME)
	}

	private Criteria toCriteria(Map map) {
		if (map.isEmpty()) {
			return new Criteria()
		}
		
		// Build criteria using fold to avoid reassignment
		map.inject(new Criteria()) { criteria, k, v ->
			criteria.and(k).is(v)
		}
	}

	Map save(Map entity) {
		mongoTemplate.save(entity, COLLECTION_NAME)
		entity
	}

	/**
	 * Batch insert multiple orders for better performance
	 */
	List saveBatch(List<Map> entities) {
		mongoTemplate.insert(entities, COLLECTION_NAME)
		entities
	}

	/**
	 * Delete orders matching criteria
	 */
	long deleteByQuery(Map mapCriteria) {
		Criteria criteria = toCriteria(mapCriteria)
		Query query = new Query(criteria)
		mongoTemplate.remove(query, COLLECTION_NAME).deletedCount
	}
}
