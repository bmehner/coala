package org.openehealth.coala.mocks.xds;

import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;

public interface RegistryRepositoryService {

	void provideAndRegister(ProvideAndRegisterDocumentSet documentSet);

	QueryResponse registryStoredQuery(FindDocumentsQuery query);

	RetrievedDocumentSet retrieveDocuments(
			RetrieveDocumentSet retrieveDocumentSet);

}