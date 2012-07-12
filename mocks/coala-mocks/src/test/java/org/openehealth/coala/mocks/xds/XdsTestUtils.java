/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openehealth.coala.mocks.xds;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.Address;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationLabel;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Author;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Name;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Organization;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Person;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Recipient;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;

public class XdsTestUtils {
	
	public static ProvideAndRegisterDocumentSet createProvideAndRegisterDocumentSet() {
		Identifiable patientID = new Identifiable("id3", new AssigningAuthority("1.3"));
		
		SubmissionSet submissionSet = createSubmissionSet(patientID);
		DocumentEntry docEntry = createDocumentEntry(patientID);
		Folder folder = createFolder(patientID);
		
		Association docAssociation = createAssociationDocEntryToSubmissionSet();
		Association folderAssociation = createAssociationFolderToSubmissionSet();
		Association docFolderAssociation = createAssociationDocEntryToFolder();
		
		URL resource = Thread.currentThread().getContextClassLoader().getResource("TestConsent.xml");
        File testConsent;
		try {
			testConsent = new File(resource.toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		
		DataHandler dataHandler = new DataHandler(new FileDataSource(testConsent));
		Document doc = new Document(docEntry, dataHandler);
		
		ProvideAndRegisterDocumentSet request = new ProvideAndRegisterDocumentSet();
		request.setSubmissionSet(submissionSet);
		request.getDocuments().add(doc);
		request.getFolders().add(folder);
		request.getAssociations().add(docAssociation);
		request.getAssociations().add(folderAssociation);
		request.getAssociations().add(docFolderAssociation);
		
		return request;
	}
	
	private static SubmissionSet createSubmissionSet(Identifiable patientID) {
        Recipient recipient = new Recipient();
        recipient.setOrganization(new Organization("org", null, null));
        
        Author author = new Author();
		author.setAuthorPerson(new Person(new Identifiable("1.2.3",
				new AssigningAuthority("1.1")),
				new Name("Riviera Nick M.D. Dr.")));
		author.getAuthorInstitution().add(
				new Organization("coalaAuthorOrg", null, null));
		author.getAuthorRole().add("Primary Care Physician");

        SubmissionSet submissionSet = new SubmissionSet();
        submissionSet.getAuthors().add(author);
        submissionSet.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        submissionSet.setComments(new LocalizedString("comments1"));
        submissionSet.setContentTypeCode(new Code("code1", new LocalizedString("code1"), "scheme1"));
        submissionSet.setEntryUuid("submissionSet01");
        submissionSet.getIntendedRecipients().add(recipient);
        submissionSet.setPatientId(patientID);
        submissionSet.setSourceId("1.2.3");
        submissionSet.setSubmissionTime("1980");
        submissionSet.setTitle(new LocalizedString("Submission Set 01", "en-US", "UTF8"));
        submissionSet.setUniqueId("123");
        return submissionSet;
    }
	
	private static DocumentEntry createDocumentEntry(Identifiable patientID) {
        Author author = new Author();
        Name name = new Name();
        name.setFamilyName("Norbi");
        author.setAuthorPerson(new Person(new Identifiable("id2", new AssigningAuthority("1.2")), name));
        author.getAuthorInstitution().add(new Organization("authorOrg", null, null));
        
        Address address = new Address();
        address.setStreetAddress("hier");
        
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setAddress(address);
        patientInfo.setDateOfBirth("12334");
        patientInfo.setGender("M");
        patientInfo.setName(new Name("Susi"));
        
        DocumentEntry docEntry = new DocumentEntry();
        docEntry.getAuthors().add(author);
        docEntry.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        docEntry.setClassCode(new Code("code2", new LocalizedString("code2"), "scheme2"));
        docEntry.setComments(new LocalizedString("comment2"));
        docEntry.getConfidentialityCodes().add(new Code("code8", new LocalizedString("code8"), "scheme8"));
        docEntry.setCreationTime("1981");
        docEntry.setEntryUuid("document01");
        docEntry.getEventCodeList().add(new Code("code9", new LocalizedString("code9"), "scheme9"));
        docEntry.setFormatCode(new Code("code3", new LocalizedString("code3"), "scheme3"));
        docEntry.setHash("1234567890123456789012345678901234567890");
        docEntry.setHealthcareFacilityTypeCode(new Code("code4", new LocalizedString("code4"), "scheme4"));
        docEntry.setLanguageCode("en-US");
        docEntry.setLegalAuthenticator(new Person(new Identifiable("legal", new AssigningAuthority("1.7")),
                new Name("Gustav")));
        docEntry.setMimeType("application/octet-stream");
        docEntry.setPatientId(patientID);
        docEntry.setPracticeSettingCode(new Code("code5", new LocalizedString("code5"), "scheme5"));
        docEntry.setRepositoryUniqueId("1.2.3.4");
        docEntry.setServiceStartTime("198012");
        docEntry.setServiceStopTime("198101");
        docEntry.setSize(123L);
        docEntry.setSourcePatientId(new Identifiable("source", new AssigningAuthority("4.1")));
        docEntry.setSourcePatientInfo(patientInfo);
        docEntry.setTitle(new LocalizedString("Document 01", "en-US", "UTF8"));
        docEntry.setTypeCode(new Code("code6", new LocalizedString("code6"), "scheme6"));
        docEntry.setUniqueId("32848902348");
        docEntry.setUri("http://hierunten.com");
        return docEntry;
    } 
	
	private static Folder createFolder(Identifiable patientID) {
        Folder folder = new Folder();
        folder.setAvailabilityStatus(AvailabilityStatus.APPROVED);
        folder.getCodeList().add(new Code("code7", new LocalizedString("code7"), "scheme7"));
        folder.setComments(new LocalizedString("comments3"));
        folder.setEntryUuid("folder01");
        folder.setLastUpdateTime("198209");
        folder.setPatientId(patientID);
        folder.setTitle(new LocalizedString("Folder 01", "en-US", "UTF8"));
        folder.setUniqueId("48574589");
        return folder;
    }
	
	private static Association createAssociationDocEntryToSubmissionSet() {
        Association docAssociation = new Association();
        docAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docAssociation.setSourceUuid("submissionSet01");
        docAssociation.setTargetUuid("document01");
        docAssociation.setLabel(AssociationLabel.ORIGINAL);
        docAssociation.setEntryUuid("docAss");
        return docAssociation;
    }
	
	private static Association createAssociationFolderToSubmissionSet() {
        Association folderAssociation = new Association();
        folderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        folderAssociation.setSourceUuid("submissionSet01");
        folderAssociation.setTargetUuid("folder01");
        folderAssociation.setEntryUuid("folderAss");
        return folderAssociation;
    }
	
	private static Association createAssociationDocEntryToFolder() {
        Association docFolderAssociation = new Association();
        docFolderAssociation.setAssociationType(AssociationType.HAS_MEMBER);
        docFolderAssociation.setSourceUuid("folder01");
        docFolderAssociation.setTargetUuid("document01");
        docFolderAssociation.setEntryUuid("docFolderAss");
        return docFolderAssociation;
    }

	
}
