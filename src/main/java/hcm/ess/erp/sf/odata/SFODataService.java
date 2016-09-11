package hcm.ess.erp.sf.odata;

import static hcm.ess.erp.sf.odata.SFODataConstants.APPLICATION_XML;
import static hcm.ess.erp.sf.odata.SFODataConstants.METADATA;
import static hcm.ess.erp.sf.odata.SFODataConstants.SEPARATOR;
import hcm.ess.erp.sf.bean.QueryInfo;
import hcm.ess.exception.ServiceApplicationException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.io.IOUtils;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SFODataService {

	@Autowired
	SFODataUtil sfODataUtil;

	private Edm edm;

	private static final Logger logger = LoggerFactory.getLogger(SFODataService.class);

	public Edm getEdm() throws ServiceApplicationException {
		return getEdm(false);
	}

	/**
	 * @param forceRefresh
	 *            force fetch metadata from server when true
	 * @return
	 * @throws ServiceApplicationException
	 */
	public Edm getEdm(boolean forceRefresh) throws ServiceApplicationException {
		if (forceRefresh || edm == null) {
			edm = readMetadata(sfODataUtil.getServiceUri());
		}
		return edm;
	}

	private Edm readMetadata(String serviceUri) throws ServiceApplicationException {
		InputStream content = null;
		try {
			String absolutUri = serviceUri + SEPARATOR + METADATA;
			HttpURLConnection connection = connect(absolutUri, APPLICATION_XML, HttpMethod.GET);
			content = connection.getInputStream();
			return EntityProvider.readMetadata(content, false);
		} catch (EntityProviderException | IOException e) {
			String msg = e.getMessage();
			logger.error("Read Metadata failed due to exception: " + msg);
			throw new ServiceApplicationException(msg, e);
		} finally {
			IOUtils.closeQuietly(content);
		}
	}

	public List<String> getNaviPropertyNames(Edm edm, String name) throws ServiceApplicationException {
		List<String> result = new ArrayList<String>();

		EdmEntityType entityType;
		try {
			entityType = edm.getEntityType("SFOData", name);
			if (entityType != null) {
				result = entityType.getNavigationPropertyNames();
			}
		} catch (EdmException e) {
			String msg = "Failed to get navigation properties from entity: " + name;
			logger.error(msg);
			throw new ServiceApplicationException(msg, e);
		}

		return result;
	}

	/**
	 * @param contentType
	 *            format of content in the given input stream known as "Accept",
	 *            e.g. "application/json"
	 * @param queryInfo
	 *            contains query options and key values
	 * @return
	 * @throws ServiceApplicationException
	 */
	public ODataEntry readEntry(String contentType, QueryInfo queryInfo) throws ServiceApplicationException {
		ODataEntry result = null;
		InputStream content = null;
		try {
			// get entity set
			EdmEntityContainer entityContainer = getEdm().getDefaultEntityContainer();
			EdmEntitySet entitySet = entityContainer.getEntitySet(queryInfo.getEntitySetName());
			// get content
			String absolutUri = createUri(sfODataUtil.getServiceUri(), queryInfo);
			HttpURLConnection connection = connect(absolutUri, contentType, HttpMethod.GET);
			content = connection.getInputStream();
			// get entry
			result = EntityProvider.readEntry(contentType, entitySet, content, EntityProviderReadProperties.init()
					.build());
		} catch (EntityProviderException | EdmException | IOException e) {
			String errMsg = "Failed to read OData entry: " + e.getMessage();
			logger.error(errMsg);
			throw new ServiceApplicationException(errMsg, e);
		} finally {
			IOUtils.closeQuietly(content);
		}
		return result;
	}

	/**
	 * @param contentType
	 *            format of content in the given input stream known as "Accept",
	 *            e.g. "application/json"
	 * @param entitySetName
	 * @param data
	 * @return
	 * @throws ServiceApplicationException
	 */
	public ODataEntry createEntry(String contentType, String entitySetName, Map<String, Object> data)
			throws ServiceApplicationException {
		String absolutUri = createUri(sfODataUtil.getServiceUri(), entitySetName, null);
		Edm edm = getEdm();
		try {
			return writeEntity(edm, absolutUri, entitySetName, data, contentType, HttpMethod.POST);
		} catch (EdmException | EntityProviderException | IOException | URISyntaxException e) {
			String errMsg = "Failed to create OData Entry " + entitySetName + ": " + e.getMessage();
			logger.error(errMsg);
			throw new ServiceApplicationException(errMsg, e);
		}
	}

	/**
	 * @param serviceUri
	 *            e.g. https://host/odata/v2
	 * @param contentType
	 *            format of content in the given input stream known as "Accept",
	 *            e.g. "application/json"
	 * @param entitySetName
	 * @param id
	 * @param data
	 * @return
	 * @throws ServiceApplicationException
	 */
	public void replaceEntry(String contentType, String entitySetName, String id, Map<String, Object> data)
			throws ServiceApplicationException {
		String absolutUri = createUri(sfODataUtil.getServiceUri(), entitySetName, id);
		Edm edm = getEdm();
		try {
			writeEntity(edm, absolutUri, entitySetName, data, contentType, HttpMethod.PUT);
		} catch (EdmException | EntityProviderException | IOException | URISyntaxException e) {
			StringBuilder msg = new StringBuilder("Failed to update OData entry ");
			msg.append(entitySetName).append("(").append(id).append(") : ").append(e.getMessage());
			logger.error(msg.toString());
			throw new ServiceApplicationException(msg.toString(), e);
		}
	}

	public HttpStatusCodes deleteEntry(String serviceUri, String entityName, String id) throws IOException {
		String absolutUri = createUri(serviceUri, entityName, id);
		HttpURLConnection connection = connect(absolutUri, APPLICATION_XML, HttpMethod.DELETE);
		return HttpStatusCodes.fromStatusCode(connection.getResponseCode());
	}

	private ODataEntry writeEntity(Edm edm, String absolutUri, String entitySetName, Map<String, Object> data,
			String contentType, String httpMethod) throws IOException, EdmException, URISyntaxException,
			EntityProviderException {
		EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		EdmEntitySet entitySet = entityContainer.getEntitySet(entitySetName);
		URI rootUri = new URI(entitySetName);
		EntityProviderWriteProperties writeProperties = EntityProviderWriteProperties.serviceRoot(rootUri).build();
		// serialize data into ODataResponse object
		ODataResponse response = EntityProvider.writeEntry(contentType, entitySet, data, writeProperties);

		HttpURLConnection connection = initializeConnection(absolutUri, contentType, httpMethod);

		// get entity for default Olingo implementation (as InputStream)
		Object entity = response.getEntity();
		if (entity instanceof InputStream) {
			byte[] buffer = streamToArray((InputStream) entity);
			connection.getOutputStream().write(buffer);
		}

		// put the newly created entity into response body
		ODataEntry entry = null;
		HttpStatusCodes statusCode = checkStatus(connection);
		if (statusCode == HttpStatusCodes.CREATED) {
			// de-serialize the content into an ODataEntry object
			InputStream content = connection.getInputStream();
			EntityProviderReadProperties readProps = EntityProviderReadProperties.init().build();
			entry = EntityProvider.readEntry(contentType, entitySet, content, readProps);
		}

		connection.disconnect();

		return entry;
	}

	/**
	 * @param contentType
	 *            format of content in the given input stream known as "Accept",
	 *            e.g. "application/json"
	 * @param queryInfo
	 *            contains query options and key values
	 * @return
	 * @throws ServiceApplicationException
	 */
	public ODataFeed readFeed(String contentType, QueryInfo queryInfo) throws ServiceApplicationException {
		ODataFeed result = null;
		InputStream content = null;
		try {
			// get entity set
			EdmEntityContainer entityContainer = getEdm().getDefaultEntityContainer();
			EdmEntitySet entitySet = entityContainer.getEntitySet(queryInfo.getEntitySetName());
			// get content
			String absolutUri = createUri(sfODataUtil.getServiceUri(), queryInfo);
			HttpURLConnection connection = connect(absolutUri, contentType, HttpMethod.GET);
			content = connection.getInputStream();
			// get feed
			result = EntityProvider.readFeed(contentType, entitySet, content, EntityProviderReadProperties.init()
					.build());
		} catch (EntityProviderException | EdmException | IOException e) {
			String errMsg = "Read OData Feed Failed: " + e.getMessage();
			logger.error(errMsg);
			throw new ServiceApplicationException(errMsg, e);
		} finally {
			IOUtils.closeQuietly(content);
		}
		return result;
	}

	private HttpURLConnection connect(String absolutUri, String contentType, String httpMethod) throws IOException {
		HttpURLConnection connection = initializeConnection(absolutUri, contentType, httpMethod);
		connection.connect();
		checkStatus(connection);
		return connection;
	}

	private HttpStatusCodes checkStatus(HttpURLConnection connection) throws IOException {
		HttpStatusCodes httpStatusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());
		if (400 <= httpStatusCode.getStatusCode() && httpStatusCode.getStatusCode() <= 599) {
			String errMsg = "Http Connection failed with status " + httpStatusCode.getStatusCode() + " "
					+ httpStatusCode.toString();
			logger.error(errMsg);
			throw new RuntimeException(errMsg);
		}
		return httpStatusCode;
	}

	private HttpURLConnection initializeConnection(String absolutUri, String contentType, String httpMethod)
			throws IOException {
		HttpURLConnection connection = null;
		URL url = new URL(absolutUri);

		if (sfODataUtil.getProxy() != null) {
			connection = (HttpURLConnection) url.openConnection(sfODataUtil.getProxy());
		} else {
			connection = (HttpURLConnection) url.openConnection();
		}

		if (connection instanceof HttpsURLConnection) {
			disableSSLVerification((HttpsURLConnection) connection);
		}
		connection.setRequestMethod(httpMethod);
		connection.setRequestProperty(HttpHeaders.ACCEPT, contentType);
		connection.setRequestProperty(HttpHeaders.AUTHORIZATION, sfODataUtil.getBasicAuth());
		if (HttpMethod.POST.equals(httpMethod) || HttpMethod.PUT.equals(httpMethod)) {
			connection.setDoOutput(true);
			connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, contentType);
		}
		return connection;
	}

	private void disableSSLVerification(HttpsURLConnection connection) {
		connection.setHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
	}

	/**
	 * @param serviceUri
	 * @param queryInfo
	 * @return
	 */
	private String createUri(String serviceUri, QueryInfo queryInfo) {
		StringBuilder sb = new StringBuilder(serviceUri);
		sb.append(SEPARATOR).append(queryInfo.getEntitySetName());
		// key value
		if (queryInfo.getKeyValue() != null) {
			sb.append("(").append(queryInfo.getKeyValue()).append(")");
		}
		// options
		Map<String, String> queryOptions = queryInfo.getQueryOptions();
		if (!queryOptions.isEmpty()) {
			boolean isFirstOption = true;
			for (Map.Entry<String, String> entry : queryOptions.entrySet()) {
				sb.append(isFirstOption ? "?" : "&");
				sb.append(entry.getKey()).append("=");
				// no white space in URI
				String encodedUri = entry.getValue().replaceAll(" ", "%20");
				sb.append(encodedUri);
				isFirstOption = false;
			}
		}
		return sb.toString();
	}

	private String createUri(String serviceUri, String entitySetName, String id) {
		QueryInfo queryInfo = new QueryInfo(entitySetName, id);
		return createUri(serviceUri, queryInfo);
	}

	private byte[] streamToArray(InputStream stream) throws IOException {
		byte[] result = new byte[0];
		byte[] tmp = new byte[8192];
		int readCount = stream.read(tmp);
		while (readCount >= 0) {
			byte[] innerTmp = new byte[result.length + readCount];
			System.arraycopy(result, 0, innerTmp, 0, result.length);
			System.arraycopy(tmp, 0, innerTmp, result.length, readCount);
			result = innerTmp;
			readCount = stream.read(tmp);
		}
		return result;
	}
}
