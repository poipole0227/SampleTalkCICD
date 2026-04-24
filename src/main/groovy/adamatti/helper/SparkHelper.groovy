package adamatti.helper

import spark.Request
import spark.Response
import spark.Spark

import static adamatti.helper.JsonHelper.toJsonString

class SparkHelper {
	static void startSpark(int port = 8080){
		Spark.port(port)

		jsonGet("/healthCheck"){Request req, Response res ->
			[status: "ok", date: new Date()]
		}

		Spark.notFound { Request req, Response res ->
			res.type("application/json")
			res.status(404)
			toJsonString([error: "not found"])
		}
	}

	static void jsonGet(String path, Closure code){
		jsonMethod(Spark.&get,path,code)
	}

	static void jsonPost(String path, Closure code){
		jsonMethod(Spark.&post,path,code)
	}

	static void jsonMethod(Closure method, String path, Closure code){
		method(path){Request req, Response res ->
			res.type("application/json")

			try {
				toJsonString code.call(req, res)
			} catch (Throwable e){
				res.status(500)
				toJsonString([error: e.message ?: e.class.simpleName ?: "Internal server error"])
			}
		}
	}
}
