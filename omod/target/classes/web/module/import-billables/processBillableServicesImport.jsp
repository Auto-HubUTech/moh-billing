<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="Manage Billable Services import" otherwise="/login.htm" redirect="/module/@MODULE_ID@/patientBillPayment.form" />

<!-- FROM GAKUBA'S MODULE: importPatientIds... -->

<%@ include file="templates/mohBillingLocalHeader.jsp"%>

<h2><spring:message code="@MODULE_ID@.billing.calculation"/></h2>

<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/listing.css" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/listingstyle.css" />

<br/>
<h2><spring:message code="@MODULE_ID@.step.3"/></h2>
<br/>

<b class="boxHeader">The process of importing <u>${recordCreatedSuccessfully+recordNotCreatedSuccessfully}</u> patient(s) identification(s) and HIV data took <u>${timeOfProcess}</u></b>
<div class="box">
	<table>
		<tr>
			<td>Date and Time started</td>
			<td> : <b><openmrs:formatDate date="${starttime}" type="long"/></b></td>
		</tr>
		<tr>
			<td>Date and Time finished</td>
			<td> : <b><openmrs:formatDate date="${endtime}" type="long"/></b></td>
		</tr>
	</table>
</div>

<br/>

<b class="boxHeader">Summary</b>
<div class="box">
	<table>
		<tr>
			<td>Number of patient created successfully</td>
			<td>: <b>${recordCreatedSuccessfully} (${recordCreatedSuccessfully*100/(recordCreatedSuccessfully+recordNotCreatedSuccessfully)}%)</b> </td>
		</tr>
		<tr>
			<td>Number of patient not created successfully</td>
			<td>
				: <b>${recordNotCreatedSuccessfully}</b>&nbsp;
				<c:if test="${recordNotCreatedSuccessfully>0}">
					(<a target="_blank" href="downloadReport.htm?fileName=${reportFileName}">Download</a>)
				</c:if>
			</td>
		</tr>
	</table>
</div>

<!-- END - FROM GAKUBA'S MODULE: importPatientIds... -->

<%@ include file="/WEB-INF/template/footer.jsp"%>
