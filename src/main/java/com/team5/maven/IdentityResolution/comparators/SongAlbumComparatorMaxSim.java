package com.team5.maven.IdentityResolution.comparators;

import java.util.Arrays;
import java.util.List;

import com.team5.maven.IdentityResolution.model.Song;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.EqualsSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.list.MaximumOfContainment;
import de.uni_mannheim.informatik.dws.winter.similarity.string.MaximumOfTokenContainment;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

public class SongAlbumComparatorMaxSim implements Comparator<Song, Attribute> {

	private static final long serialVersionUID = 1L;
	private MaximumOfContainment<String> sim = new MaximumOfContainment<String>();
	private ComparatorLogger comparisonLog;

	@Override
	public double compare(
			Song record1,
			Song record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
		
    	String s1 = record1.getAlbum().toString().toLowerCase();
		String s2 = record2.getAlbum().toString().toLowerCase();

		if(s1.isEmpty() || s2.isEmpty()) {
			return 0;
		}
		
		List<String> albums1 = Arrays.asList(s1.split(","));
		List<String> albums2 = Arrays.asList(s2.split(","));

		
    	double similarity = sim.calculate(albums1, albums2);
    	
		if(this.comparisonLog != null){
			this.comparisonLog.setComparatorName(getClass().getName());
		
			this.comparisonLog.setRecord1Value(s1);
			this.comparisonLog.setRecord2Value(s2);
    	
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
