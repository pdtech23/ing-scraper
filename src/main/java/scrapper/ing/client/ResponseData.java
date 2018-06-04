package scrapper.ing.client;

import org.apache.http.Header;
import org.json.JSONObject;

public class ResponseData {


    private final JSONObject jsonBody;

    public JSONObject getJsonBody() {
        return jsonBody;
    }

    public Header[] getHeaders() {
        return headers;
    }

    private final Header[] headers;

    public ResponseData(JSONObject jsonBody, Header[] headers) {
        this.jsonBody = jsonBody;
        this.headers = headers;
    }
}
