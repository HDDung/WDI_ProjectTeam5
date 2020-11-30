package com.team5.maven.IdentityResolution.comparators;

import java.util.Arrays;
import java.util.List;

import com.team5.maven.IdentityResolution.model.Song;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.list.OverlapSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

public class SongArtistCustomizedComparator implements Comparator<Song, Attribute>{
	
	private static final long serialVersionUID = 1L;
	private TokenizingJaccardSimilarity sim_jacc = new TokenizingJaccardSimilarity();
	private OverlapSimilarity sim_over = new OverlapSimilarity();
	private ComparatorLogger comparisonLog;
	
	@Override
	public double compare(
			Song record1,
			Song record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
		
    	String s1 = record1.getArtist().toLowerCase();
		String s2 = record2.getArtist().toLowerCase();
		List<String> items_s1 = Arrays.asList(s1.split("\\s*,\\s*"));
		List<String> items_s2 = Arrays.asList(s2.split("\\s*,\\s*"));

//		System.out.println(items_s1);
		
    	double similarity = (sim_jacc.calculate(s1, s2) + sim_over.calculate(items_s1, items_s2))/2;
    	
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
