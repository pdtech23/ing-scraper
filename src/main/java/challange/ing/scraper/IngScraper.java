package challange.ing.scraper;

import challange.account.Account;
import challange.ing.scraper.request.RequestCreator;
import challange.ing.scraper.response.Response;
import challange.ing.scraper.response.ResponseHandler;
import challange.ing.scraper.response.exception.InvalidResponseException;
import challange.ing.security.session.AuthenticatedSession;
import challange.ing.security.session.UnauthenticatedSession;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class IngScraper implements IIngScraper {

  private ResponseHandler responseHandler;
  private RequestCreator requestCreator;

  public IngScraper() {
    responseHandler = new ResponseHandler();
    requestCreator = new RequestCreator();
  }

  @Override
  public UnauthenticatedSession fetchUnauthenticatedSession(String login) {
    HttpPost request = requestCreator.createRequestForLoginCheck(login);
    Response response = executeJsonRequest(request);
    return responseHandler.extractUnauthenticatedSession(response);
  }

  @Override
  public AuthenticatedSession fetchAuthenticatedSession(String login, char[] password, UnauthenticatedSession unauthenticatedSession) {
    HttpPost request = requestCreator.createRequestForLogin(login, password, unauthenticatedSession);
    Response response = executeJsonRequest(request);
    return responseHandler.extractAuthenticatedSession(response);
  }

  @Override
  public List<Account> fetchAccounts(AuthenticatedSession authenticatedSession) {
    HttpPost request = requestCreator.createRequestForFetchingAccounts(authenticatedSession);
    Response response = executeJsonRequest(request);
    return responseHandler.extractAccounts(response);
  }

  private Response executeJsonRequest(HttpPost httpPost) {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      CloseableHttpResponse response = client.execute(httpPost);
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
}
