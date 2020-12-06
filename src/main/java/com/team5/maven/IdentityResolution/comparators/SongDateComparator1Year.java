package com.team5.maven.IdentityResolution.comparators;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.date.YearSimilarity;
import com.team5.maven.IdentityResolution.model.Song;

public class SongDateComparator1Year implements Comparator<Song, Attribute> {

	private static final long serialVersionUID = 1L;
	private YearSimilarity sim = new YearSimilarity(1);
	
	private ComparatorLogger comparisonLog;

	@Override
	public double compare(
			Song record1,
			Song record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
    	
    	double similarity = sim.calculate(record1.getYear(), record2.getYear());
    	
		if(this.comparisonLog != null){
			this.comparisonLog.setComparatorName(getClass().getName());
		
			this.comparisonLog.setRecord1Value(record1.getYear().toString());
			this.comparisonLog.setRecord2Value(record2.getYear().toString());
    	
			this.comparisonLog.setSimilarity(Double.toString(similarity));
		}
		return similarity;

	}

	@Override
	public ComparatorLogger getComparisonLog() {
		return this.comparisonLog;
	}

	@Override
	public void setComparisonLog(ComparatorLogger comparatorLog) {
		this.comparisonLog = comparatorLog;
	}

}
