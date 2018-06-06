package scrapper.ing.client.response;

import org.apache.http.Header;
import org.json.JSONObject;

public class ResponseData {


    private final JSONObject jsonBody;
    private final Header[] headers;

    public ResponseData(JSONObject jsonBody, Header[] headers) {
        this.jsonBody = jsonBody;
        this.headers = headers;
    }

    public JSONObject getJsonBody() {
        return this.jsonBody;
    }

    public Header[] getHeaders() {
        return this.headers;
    }
}
