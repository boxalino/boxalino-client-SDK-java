package boxalino.client.SDK;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import com.boxalino.p13n.api.thrift.AutocompleteRequest;
import com.boxalino.p13n.api.thrift.AutocompleteRequestBundle;
import com.boxalino.p13n.api.thrift.ChoiceRequest;

public class HttpDecorator implements HttpRequestInterceptor {

	private static final String PROFILEID_HEADER = "X-BX-PROFILEID";
	
	private final AtomicReference<String> nextProfileId = new AtomicReference<>();
	
	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		String profileId = nextProfileId.getAndSet(null);
		if (profileId != null) {
			request.addHeader(PROFILEID_HEADER, profileId);
		}
	}
	
	public void beforeSend(Object o) {
		if (o instanceof ChoiceRequest) {
			ChoiceRequest cr = (ChoiceRequest) o;
			setNextProfileId(cr.getProfileId());
		} else if (o instanceof AutocompleteRequest) {
			AutocompleteRequest acr = (AutocompleteRequest) o;
			setNextProfileId(acr.getProfileId());
		} else if (o instanceof AutocompleteRequestBundle) {
			AutocompleteRequestBundle acrb = (AutocompleteRequestBundle) o;
			List<AutocompleteRequest> requests = acrb.getRequests();
			if (requests == null || requests.isEmpty()) return;
			
			setNextProfileId(requests.get(0).getProfileId());
		}
	}
	
	public void setNextProfileId(String profileId) {
		nextProfileId.set(profileId);
	}

}
