<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="Manage Billable Services import" otherwise="/login.htm" redirect="/module/@MODULE_ID@/patientBillPayment.form" />

<!-- Put here the billing template header... -->

<h2><spring:message code="mohbilling.billing.import.billables"/></h2>

<!-- FROM GAKUBA'S MODULE: importPatientIds... -->

<h2 style="color: blue;">Step 1 : Locate the excel file containing Tariff to import</h2>

<i style="color: red; margin-left: 2px;"><b>Rmq:</b> The excel file to import must be in a 97-2003 format</i>

<br/>
<form class="box" action="importbillableservices.form" method="post">
	<table>
		<tr>
			<td style="vertical-align: top;">File to import</td>
			<td style="vertical-align: top;">
				<input type="text" name="fileToImport" size="80" value="${fileToImport}"/>
				<br/><i style="color: red;">C:\\folder\\fileToImport.xls (for Windows) OR /home/user/fileToImport.xls (for Linux)</i>
			</td>
			<td style="vertical-align: top;"><input type="submit" value="View & Analyse File Content"/></td>
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
							<th class="columnHeader">#</th>
							<th class="columnHeader">Actes médicaux</th>
							<th class="columnHeader">Mutuelle communautaire</th>
							<th class="columnHeader">RSSB,MMI, MUTUELLES D'UNIVERSITES ET INSTITUTIONS SUPERIEURES</th>
							<th class="columnHeader">Assurance privee, Societe commerciale, Autres</th>
							<th class="columnHeader">Sans couverture par assurance valable</th>
							<th class="columnHeader">OpenMRS concept ID</th>
							<th><input id="deselect_button" type="button" value="Deselect All" name="deselect_button"/></th>
						</tr>
						<c:if test="${empty result}"><tr><td colspan="6" width="100%"  style="text-align: center; ">No data read !</td></tr></c:if>
						<c:forEach items="${result}" var="rowContent" varStatus="status">
							<tr>
								<td>${status.count}.</td>
								<c:forEach items="${rowContent}" var="cellContent" varStatus="counter">
									<td class="rowValue ${status.count%2!=0?'even':''}">${cellContent}</td>
								</c:forEach>
								<td><input id="box_${status.count}" type="checkbox" checked="checked" name="box_${cellContent}"/></td>
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
var $hfe = jQuery.noConflict();

	function submitForm(){
		if (confirm("Are you sure you want to start importing ?")) {
			document.getElementById("form_import_patient_identification").submit();
	    }
	}
	
	$hfe('input[type=checkbox]').attr('checked',true);
    $hfe('input[type=button]').change(function(){
             if($hfe('button#deselect_button').clicked()){
                    $hfe('span#is_opportunistic input[type=checkbox]').attr('disabled',false);
             }
     });
	
</script>

<!-- END - FROM GAKUBA'S MODULE: importPatientIds... -->

<%@ include file="/WEB-INF/template/footer.jsp"%>
