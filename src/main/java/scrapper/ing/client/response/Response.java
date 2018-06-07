package scrapper.ing.client.response;

import org.apache.http.Header;
import org.json.JSONObject;

public class Response {

    public static final Response EMPTY_RESPONSE = new Response(new JSONObject(), new Header[0]);

    private JSONObject jsonBody;
    private Header[] headers;

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
