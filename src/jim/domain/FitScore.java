package jim.domain;

import java.math.BigDecimal;

public class FitScore {
	private String distance;
	private BigDecimal pace;
	private BigDecimal mph;
	private String time;
	private BigDecimal fitScore;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	
	public BigDecimal getMph() {
		return mph;
	}
	public void setMph(BigDecimal mph) {
		this.mph = mph;
	}
	public BigDecimal getFitScore() {
		return fitScore;
	}
	public void setFitScore(BigDecimal fitScore) {
		this.fitScore = fitScore;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public BigDecimal getPace() {
		return pace;
	}
	public void setPace(BigDecimal pace) {
		this.pace = pace;
	}	
}
