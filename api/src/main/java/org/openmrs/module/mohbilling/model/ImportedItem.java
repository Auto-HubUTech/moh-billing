/**
 * 
 */
package org.openmrs.module.mohbilling.model;

/**
 * @author Kamonyo
 * 
 */
public class ImportedItem {

	private String medicalAct;
	private Double tariffA;
	private Double tariffB;
	private Double tariffC;
	private Double tariffD;
	private Integer conceptId;
	private boolean chosen;

	public ImportedItem(String medicalAct, Double tariffA, Double tariffB,
			Double tariffC, Double tariffD, Integer conceptId, boolean chosen) {
		super();
		this.medicalAct = medicalAct;
		this.tariffA = tariffA;
		this.tariffB = tariffB;
		this.tariffC = tariffC;
		this.tariffD = tariffD;
		this.conceptId = conceptId;
		this.chosen = chosen;
	}

	/**
	 * @return the medicalAct
	 */
	public String getMedicalAct() {
		return medicalAct;
	}

	/**
	 * @param medicalAct
	 *            the medicalAct to set
	 */
	public void setMedicalAct(String medicalAct) {
		this.medicalAct = medicalAct;
	}

	/**
	 * @return the tariffA
	 */
	public Double getTariffA() {
		return tariffA;
	}

	/**
	 * @param tariffA
	 *            the tariffA to set
	 */
	public void setTariffA(Double tariffA) {
		this.tariffA = tariffA;
	}

	/**
	 * @return the tariffB
	 */
	public Double getTariffB() {
		return tariffB;
	}

	/**
	 * @param tariffB
	 *            the tariffB to set
	 */
	public void setTariffB(Double tariffB) {
		this.tariffB = tariffB;
	}

	/**
	 * @return the tariffC
	 */
	public Double getTariffC() {
		return tariffC;
	}

	/**
	 * @param tariffC
	 *            the tariffC to set
	 */
	public void setTariffC(Double tariffC) {
		this.tariffC = tariffC;
	}

	/**
	 * @return the tariffD
	 */
	public Double getTariffD() {
		return tariffD;
	}

	/**
	 * @param tariffD
	 *            the tariffD to set
	 */
	public void setTariffD(Double tariffD) {
		this.tariffD = tariffD;
	}

	/**
	 * @return the conceptId
	 */
	public Integer getConceptId() {
		return conceptId;
	}

	/**
	 * @param conceptId
	 *            the conceptId to set
	 */
	public void setConceptId(Integer conceptId) {
		this.conceptId = conceptId;
	}

	/**
	 * @return the chosen
	 */
	public boolean isChosen() {
		return chosen;
	}

	/**
	 * @param chosen the chosen to set
	 */
	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}

}
