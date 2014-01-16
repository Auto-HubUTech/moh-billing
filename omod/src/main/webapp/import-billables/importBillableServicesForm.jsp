<%@page import="org.openmrs.module.mohbilling.model.ImportedItem"%>
<%@page import="java.util.List"%>
<%@page import="org.openmrs.module.mohbilling.model.BillableService"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:require privilege="Manage Billable Services import" otherwise="/login.htm" redirect="/module/@MODULE_ID@/patientBillPayment.form" />

<!-- Put here the billing template header... -->

<h2><spring:message code="mohbilling.billing.import.billables"/></h2>

<!-- FROM GAKUBA'S MODULE: importPatientIds... -->

<h2 style="color: blue;">Step 1 : Locate the excel file containing Tariff to import</h2>

<i style="color: red; margin-left: 2px;"><b>Rmq:</b> The excel file to import must be in a 97-2003 format</i>

<br/>
<form class="box" action="importbillableservices.form" method="post" id="import_billables_form">
	<table>
		<tr>
			<td style="vertical-align: top;">File to import</td>
			<td style="vertical-align: top;">
				<input class="importField" type="text" id="fileToImport" name="fileToImport" size="80" value="${fileToImport}"/>
				<br/><i style="color: red;">C:\\folder\\fileToImport.xls (for Windows) OR /home/user/fileToImport.xls (for Linux)</i>
			</td>
			<td style="vertical-align: top;"><input type="submit" value="View & Analyse File Content"/></td>
		</tr>
	</table>
</form>
<br/>
<%
	java.util.List<org.openmrs.module.mohbilling.model.ImportedItem> items = new java.util.ArrayList<org.openmrs.module.mohbilling.model.ImportedItem>();
%>
<%!	
	private void getItemByConcept(boolean status, String checkBox,
			List<ImportedItem> items) {
		
		String box = (String)checkBox;//in case we use Object
		String[] temp = checkBox.split("chosen_");
		Integer conceptId = Integer.parseInt(temp[0]);
	
		for (ImportedItem item : items) {
			if (item.getConceptId().equals(conceptId)) {
				item.setChosen(status);
			}
		}
	}
%>
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
							
							<c:set var="medAct" scope="session" value="${rowContent.medicalAct}"/>
							<c:set var="tarifA" scope="session" value="${rowContent.tariffA}"/>
							<c:set var="tarifB" scope="session" value="${rowContent.tariffB}"/>
							<c:set var="tarifC" scope="session" value="${rowContent.tariffC}"/>
							<c:set var="tarifD" scope="session" value="${rowContent.tariffD}"/>
							<c:set var="concept" scope="session" value="${rowContent.conceptId}"/>
							<c:set var="choice" scope="session" value="${rowContent.chosen}"/>
							
							<tr>
								<td>${status.count}.</td>
								<td>${rowContent.medicalAct}<input type="hidden" name="medicalAct_${rowContent.conceptId}"/></td>
								<td>${rowContent.tariffA}<input type="hidden" name="tariffA_${rowContent.conceptId}"/></td>
								<td>${rowContent.tariffB}<input type="hidden" name="tariffB_${rowContent.conceptId}"/></td>
								<td>${rowContent.tariffC}<input type="hidden" name="tariffC_${rowContent.conceptId}"/></td>
								<td>${rowContent.tariffD}<input type="hidden" name="tariffD_${rowContent.conceptId}"/></td>
								<td>${rowContent.conceptId}<input type="hidden" name="conceptId_${rowContent.conceptId}"/></td>
								
								<%
									String medicalAct = session.getAttribute("medAct").toString();
									Double tariffA = Double.parseDouble(session.getAttribute("tarifA").toString());
									Double tariffB = Double.parseDouble(session.getAttribute("tarifA").toString());
									Double tariffC = Double.parseDouble(session.getAttribute("tarifA").toString());
									Double tariffD = Double.parseDouble(session.getAttribute("tarifA").toString());
									Integer conceptId = Integer.parseInt(session.getAttribute("concept").toString());
									boolean chosen = Boolean.parseBoolean(session.getAttribute("choice").toString());
									
									items.add(new ImportedItem(medicalAct,tariffA,tariffB,tariffC,tariffD,conceptId,chosen));
								%>
								
								<td>
									<span id="checkIt" class="selectedBox">
										<input name="selectedItem" type="hidden" value="${rowContent.conceptId}"/>
										<input class="selectedBox" id="box_${status.count}" type="checkbox" <c:if test='${rowContent.chosen}' >checked="checked"</c:if> name="chosen_${rowContent.conceptId}"/>
									</span>
								</td>
							</tr>
							
						</c:forEach>
						<%
							session.setAttribute("itemsList", items);
						%>
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
		<form class="box" action="processbillableimport.form" method="post" id="form_import_billables">
			<table>
				<tr>
					<td><input type="hidden" name="sourceFile" size="40" value="${fileToImport}"/>
						<input type="hidden" name="includeFirstRow" value="${columnHeaderChecked}"/>
						<input type="hidden" name="numberOfRecords" value="${numberOfPatientsToImport+((columnHeaderChecked)?0:1)}"/>
					</td>
					<td><input type="button" onclick="submitForm();" value="Import Billables"/></td>
					<td></td>
				</tr>
			</table>
		</form>
	
	</div>
</c:if>

<script type="text/javascript">
	var $hfe = jQuery.noConflict();
	
	$hfe(document).ready(function() {
		var value = $hfe('#deselect_button').attr('value');
		
	    $hfe('#deselect_button').click(function(){
	    	
	    	if(value == 'Deselect All'){
	    		if($hfe('input[type=checkbox]').is(':checked')){
		    		$hfe('input[type=checkbox]').attr('checked',false);
			    	$hfe('#deselect_button').attr('value','Select All');
					value = 'Select All';
	    		}
	    	}

	    	else if(value == 'Select All'){
				if(!$hfe('input[type=checkbox]').is(':checked')){
					$hfe('input[type=checkbox]').attr('checked','checked');
					$hfe('#deselect_button').attr('value','Deselect All');
					value = 'Deselect All';
	    		}
			}
	    });
	    
	    $hfe('.selectedBox').click(function(){

    		var boxName = $hfe('input[type=checkbox]').attr('name');
			
    		//TODO: chercher un moyen de faire changer le "action" et avoir les resultats attendus.
    		
			if($hfe('input[type=checkbox]').is(':checked')=='false'){
				<%
					System.out.println("__________________I am reaching here in JSP page");
					session.setAttribute("selected", true);
					session.setAttribute("myName", "chosen_6983");
				%>
				var fileToImport = $hfe('#fileToImport').val();
				$hfe('#fileToImport').val('');
	    		$hfe('#import_billables_form').attr('action','importbillableservices.form?selected=false&myName='+boxName+'&fileToImport='+fileToImport);
	    		$hfe('#import_billables_form').attr('method','get');
				alert("I get this when it is UNCHECKED...");
				document.getElementById("import_billables_form").submit();
				
				
				$hfe('#import_billables_form').attr('method','post');
	    		$hfe('#import_billables_form').attr('action','importbillableservices.form');
				alert("I get this when it is CHECKED...");
			}else{
	    		$hfe('#import_billables_form').attr('method','get');
	    		$hfe('#import_billables_form').attr('action','importbillableservices.form?selected=true&myName='+boxName);
				document.getElementById("import_billables_form").submit();
				alert("I get this when it is UNCHECKED...");
	    	}
	    });

    });
	
	function getItem(myBox){
		alert("I get this first..."+myBox);
		<!-- Try to run this Java Method here... -->
		//getItemByConcept(true, "chosen_6983", items);
		alert("I've got there");
	}

	function submitForm(){
		if (confirm("Are you sure you want to start importing ?")) {
			document.getElementById("form_import_billables").submit();
	    }
	}
	

	function loadXMLDoc()
	{
		
	}
</script>

<style>

.selectedBox{
		background-color: white;
	}

.importField{
		background-color: white;
	}
</style>

<!-- END - FROM GAKUBA'S MODULE: importPatientIds... -->

<%@ include file="/WEB-INF/template/footer.jsp"%>
