<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="Manage Billable Services import" otherwise="/login.htm" redirect="/module/@MODULE_ID@/patientBillPayment.form" />

<%@ include file="templates/mohBillingLocalHeader.jsp"%>

<h2><spring:message code="@MODULE_ID@.billing.calculation"/></h2>

<!-- FROM GAKUBA'S MODULE: importPatientIds... -->

<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/listing.css" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/listingstyle.css" />

<h2><c:if test="${null==result}"><spring:message code="@MODULE_ID@.step.1"/></c:if><c:if test="${null!=result}"><spring:message code="@MODULE_ID@.step.2"/></c:if></h2>

<i style="color: red; margin-left: 2px;"><b>Rmq:</b> The excel file to import must be in a 97-2003 format</i>

<br/>
<form class="box" action="importpatientidentification.form" method="post">
	<table>
		<tr>
			<td style="vertical-align: top;">File to import</td>
			<td style="vertical-align: top;">
				<input type="text" name="fileToImport" size="80" value="${fileToImport}"/>
				<br/><i style="color: red;">C:\\folder\\fileToImport.xls</i>
			</td>
			<td style="vertical-align: top;"><input type="submit" value="View/Analyse File Content"/></td>
		</tr>
		<tr>
			<td>Take first row as column headers</td>
			<td><input type="checkbox" name="columnHeader" <c:if test="${!columnHeaderChecked}">checked='checked'</c:if>/></td>
			<td></td>
		</tr>
	</table>
</form>
<br/>
<c:if test="${null!=result}">
	<div>
	
		<form class="box" action="processDataImport.form" method="post" id="form_import_patient_identification">
			<table>
				<tr>
					<td>Location</td>
					<td><select name="location">
							<c:forEach items="${locations}" var="loc"><option value="${loc.key}">${loc.value}</option></c:forEach>
						</select>
					</td>
					<td></td>
				</tr>
				<tr>
					<td>Local Patient Identifier Type</td>
					<td><select name="localIdentifierType">
							<c:forEach items="${pIdentifierTypes}" var="pIdType"><option value="${pIdType.key}">${pIdType.value}</option></c:forEach>
						</select>
					</td>
					<td style="padding-left: 30px;"><input type="checkbox" name="includeLocalIdentifierType"/> Include this Identifier</td>
				</tr>
				<tr>
					<td>Tracnet Identifier Type</td>
					<td><select name="tracnetIdentifierType">
							<c:forEach items="${pIdentifierTypes}" var="pIdentifierType"><option value="${pIdentifierType.key}">${pIdentifierType.value}</option></c:forEach>
						</select>
					</td>
					<td></td>
				</tr>
				<tr>
					<td>ARV Identifier Type</td>
					<td><select name="arvIdentifierType">
							<c:forEach items="${pIdentifierTypes}" var="pType"><option value="${pType.key}">${pType.value}</option></c:forEach>
						</select>
					</td>
					<td style="padding-left: 30px;"><input type="checkbox" name="includeArvIdentifierType"/> Include this Identifier</td>
				</tr>
				<tr>
					<td><input type="hidden" name="sourceFile" size="40" value="${fileToImport}"/>
						<input type="hidden" name="includeFirstRow" value="${columnHeaderChecked}"/>
						<input type="hidden" name="numberOfRecords" value="${numberOfPatientsToImport+((columnHeaderChecked)?0:1)}"/>
					</td>
					<td><input type="button" onclick="submitForm();" value="Import Data"/></td>
					<td></td>
				</tr>
			</table>
		</form>
		<br/>
		
		<div id="data" style="margin: auto; width: 99%; font-size: 0.9em">
			<div id="list_container" style="width: 99%; padding-top: 4px;">
				<div id="list_title">
					<div class="list_title_msg">Excel File Content <c:if test="${!empty result}"> :: ${numberOfPatientsToImport} rows</c:if></div>
					<div class="list_title_bts">
						&nbsp;&nbsp;&nbsp;
					</div>
					<div style="clear:both;"></div>
				</div>
				<div style="width: 100%; max-height: 500px; overflow: scroll;">
					<table id="list_data" border="1">
						<tr>
							<th rowspan="2" class="columnHeader">#</th>
							<th rowspan="2" class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be a String (Optional)"/>Local ID</th>
							<th rowspan="2" class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be a six digit Number (Mandatory)"/>No Tracnet</th>
							<th rowspan="2" class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be a Number (Optional)"/>ARV Number</th>
							<th colspan="4" class="columnHeader"><center>Patient Identification</center></th>
							<th colspan="4" class="columnHeader"><center>Address</center></th>
							<th colspan="3" class="columnHeader"><center>HIV Program</center></th>
							<th colspan="2" class="columnHeader"><center>Patient Exit Care</center></th>
						</tr>		
						<tr>
							<th class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be an Open text (Mandatory)"/>Nom</th>
							<th class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be an Open text (Mandatory)"/>Prenom</th>
							<th class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be a "/>DoB</th>
							<th class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be a "/>Gender</th>
							<th class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be an Open text (Optional)"/>District</th>
							<th class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be an Open text (Optional)"/>Secteur</th>
							<th class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be an Open text (Optional)"/>Cellule</th>
							<th class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be an Open text (Optional)"/>Umudugudu</th>
							<th class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be a Date with (dd/MM/yyyy) as format (Mandatory)"/>Date Enrollement</th>
							<th class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be [1 or PMTCT], [2 or VCT], [3 or Hospitalisation], [4 or Transfer in], [5 or Outpatient Consultation] or [6 or Other] (If not provided, it will be taken as Unknown)"/>Source</th>
							<th class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be a Date with (dd/MM/yyyy) as format (Optional)"/>ARV Startdate</th>
							<th class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be a Date with (dd/MM/yyyy) as format (Optional)"/>Date</th>
							<th class="columnHeader"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="This should be [1 or Died], [2 or Defaulted], [3 or HIV Negative] or [4 or Transferred] (Optional but should be provided in case you provided the date the patient exited care)"/>Reason</th>
						</tr>
						<c:if test="${empty result}"><tr><td colspan="16" width="100%"  style="text-align: center; ">No data found !</td></tr></c:if>
						<c:forEach items="${result}" var="rowContent" varStatus="status">
							<tr>
								<td>${status.count}.</td>
								<c:forEach items="${rowContent}" var="cellContent" varStatus="counter">
									<td class="rowValue ${status.count%2!=0?'even':''}" style="<c:if test="${(((counter.count==2) || (counter.count==4) || (counter.count==5) || (counter.count==6) || (counter.count==7) || (counter.count==12)) && (cellContent=='' || cellContent==null))}">background-color: red</c:if>">${cellContent}</td>
								</c:forEach>
							</tr>
						</c:forEach>
					</table>
				</div>
			
				<div id="list_footer">
					<div class="list_footer_info">&nbsp;&nbsp;&nbsp;</div>
					<div class="list_footer_pages">		
						&nbsp;&nbsp;&nbsp;	
					</div>
					<div style="clear: both"></div>
				</div>
			</div>
				
		</div>
	
	</div>
</c:if>

<script type="text/javascript">
	function submitForm(){
		if (confirm("Are you sure you want to start importing ?")) {
			document.getElementById("form_import_patient_identification").submit();
	    }
	}
</script>

<!-- END - FROM GAKUBA'S MODULE: importPatientIds... -->

<%@ include file="/WEB-INF/template/footer.jsp"%>
