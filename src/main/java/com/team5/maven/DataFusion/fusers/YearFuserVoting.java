package com.team5.maven.DataFusion.fusers;

import java.time.LocalDateTime;

import com.team5.maven.IdentityResolution.model.Song;

import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.Voting;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class YearFuserVoting extends AttributeValueFuser<LocalDateTime, Song, Attribute> {

	public YearFuserVoting() {
		super(new Voting<LocalDateTime, Song, Attribute>());
	}
	
	@Override
	public boolean hasValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Song.YEAR);
	}
	
	@Override
	public LocalDateTime getValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getYear();
	}

	@Override
	public void fuse(RecordGroup<Song, Attribute> group, Song fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<LocalDateTime, Song, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setYear(fused.getValue());
		fusedRecord.setAttributeProvenance(Song.YEAR, fused.getOriginalIds());
	}

}
