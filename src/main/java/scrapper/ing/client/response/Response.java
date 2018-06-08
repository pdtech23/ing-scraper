package scrapper.ing.client.response;

import org.apache.http.Header;
import org.json.JSONObject;

public class Response {

    private final JSONObject jsonBody;
    private final Header[] headers;

    public Response(JSONObject jsonBody, Header[] headers) {
        this.jsonBody = jsonBody;
        this.headers = headers;
    }

    JSONObject getJsonBody() {
        return this.jsonBody;
    }

    Header[] getHeaders() {
        return this.headers;
    }
}
