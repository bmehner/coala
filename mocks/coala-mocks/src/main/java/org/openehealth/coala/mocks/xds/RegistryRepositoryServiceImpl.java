package org.openehealth.coala.mocks.xds;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.activation.DataHandler;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocument;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;

public class RegistryRepositoryServiceImpl implements RegistryRepositoryService {
	
	private ConcurrentHashMap<Identifiable, LinkedList<DocumentEntry>> registry = new ConcurrentHashMap<Identifiable, LinkedList<DocumentEntry>>();
	private ConcurrentHashMap<String, DataHandler> repository = new ConcurrentHashMap<String, DataHandler>();
	
	/* (non-Javadoc)
	 * @see org.openehealth.coala.mocks.xds.RegistryRepositoryService#provideAndRegister(org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet)
	 */
	@Override
	public void provideAndRegister(ProvideAndRegisterDocumentSet documentSet) {
		List<Document> documents = documentSet.getDocuments();
		for (Document document : documents) {
			DocumentEntry documentEntry = document.getDocumentEntry();
			DataHandler content = document.getContent(DataHandler.class);
			Identifiable patientId = documentEntry.getPatientId();
			LinkedList<DocumentEntry> patientsDocuments = registry.get(patientId);
			if (patientsDocuments != null) {
				patientsDocuments.addLast(documentEntry);
			} else {
				patientsDocuments = new LinkedList<DocumentEntry>();
				patientsDocuments.addLast(documentEntry);
			}
			registry.put(patientId, patientsDocuments);
			repository.put(documentEntry.getUniqueId(), content);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.openehealth.coala.mocks.xds.RegistryRepositoryService#registryStoredQuery(org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery)
	 */
	@Override
	public QueryResponse registryStoredQuery(FindDocumentsQuery query) {
		Identifiable patientId = query.getPatientId();
		List<DocumentEntry> documentEntries = registry.get(patientId);
		QueryResponse queryResponse = new QueryResponse();
		queryResponse.setDocumentEntries(documentEntries);
		queryResponse.setStatus(Status.SUCCESS);
		return queryResponse;
	}
	
	/* (non-Javadoc)
	 * @see org.openehealth.coala.mocks.xds.RegistryRepositoryService#retrieveDocuments(org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet)
	 */
	@Override
	public RetrievedDocumentSet retrieveDocuments(RetrieveDocumentSet retrieveDocumentSet) {
		List<RetrievedDocument> retrievedDocuments = new ArrayList<RetrievedDocument>();
		List<RetrieveDocument> documents = retrieveDocumentSet.getDocuments();
		for (RetrieveDocument retrieveDocument : documents) {
			String documentUniqueId = retrieveDocument.getDocumentUniqueId();
			RetrievedDocument retrievedDocument= new RetrievedDocument();
			retrievedDocument.setRequestData(retrieveDocument);
			retrievedDocument.setDataHandler(repository.get(documentUniqueId));
		}
		RetrievedDocumentSet retrievedDocumentSet = new RetrievedDocumentSet(Status.SUCCESS, retrievedDocuments);

		return retrievedDocumentSet;
	}
	
}
