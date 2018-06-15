package challange.ing.scraper.fetching;

import challange.ing.scraper.response.Response;
import challange.ing.scraper.response.exception.InvalidResponseException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UncheckedIOException;

public abstract class SingleRequestFetchingStrategy<T> implements FetchingStrategy {

  protected abstract T extractResultFrom(Response response);

  protected abstract HttpUriRequest prepareRequest();

  private Response executeRequest(HttpUriRequest request) {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      CloseableHttpResponse response = client.execute(request);
      return new Response(extractResponseJson(response), response.getAllHeaders());
    } catch (JSONException e) {
      throw new InvalidResponseException(e);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private JSONObject extractResponseJson(HttpResponse response) throws IOException, JSONException {
    String jsonAsString = EntityUtils.toString(response.getEntity());
    return new JSONObject(jsonAsString);
  }

  @Override
  public T fetch() {
    HttpUriRequest request = prepareRequest();
    Response response = executeRequest(request);
    return extractResultFrom(response);
  }
}
