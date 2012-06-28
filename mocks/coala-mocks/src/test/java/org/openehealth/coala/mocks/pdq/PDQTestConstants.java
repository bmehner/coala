/*
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
 */
package org.openehealth.coala.mocks.pdq;

public interface PDQTestConstants {

//	String VALID_REQUEST_HEADER = "MSH|^~\\&|OHFConsumer|OHFFacility|OTHER_KIOSK|HIMSSSANDIEGO|20070108145322-0800||"
//		+ "QBP^Q22|9416994431147258002|P|2.5|\nQPD|Q22^Find Candidates|"
//		+ "7891956360974608557281601076319|@PID.5.1^M*|\nRCP|I|2^RD";
	
	String VALID_REQUEST_HEADER = "MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|PIM|"
			+ "20081031112704||QBP^Q22|324406609|P|2.5|||ER|||||\n"
			+ "QPD|IHE PDQ Query|1402274727|@PID.3.1^12345678~@PID.3.2.1^BLABLA~@PID.3.4.2^1.2.3.4~@PID.3.4.3^KRYSO|||||\n"
			+ "RCP|I|10^RD|||||\n"
			;
	
	String INVALID_REQUEST_HEADER = "MSH|^~\\&||HIMSSSANDIEGO|0||"
		+ "QBP^Q22|9416994431147258002|P|2.5|\n|"
		+ "7891956360974608557281601076319|@PID.5.1^M*|\nRCP|I|2^RD";
	
	String VALID_PDQ_MESSAGE = "MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|PIM|" +
            "20081031112704||QBP^Q22|324406609|P|2.5|||ER|||||\n" +
            "QPD|IHE PDQ Query|1402274727|@PID.3.1^12345678~@PID.3.2.1^BLABLA~@PID.3.4.2^1.2.3.4~@PID.3.4.3^KRYSO|||||\n" +
            "RCP|I|10^RD|||||\n";
}
