package com.team5.maven.DataFusion.fusers;

import com.team5.maven.DataFusion.fusers.utility.ModifiedAverage;
import com.team5.maven.IdentityResolution.model.Song;

import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class DurationFuserAverage extends
		AttributeValueFuser<Double, Song, Attribute>{

	public DurationFuserAverage() {
		super(new ModifiedAverage<Song, Attribute>());
	}


	@Override
	public boolean hasValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Song.DURATION);
	}
	
	@Override
	public Double getValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getDuration();
	}

	@Override
	public void fuse(RecordGroup<Song, Attribute> group, Song fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<Double, Song, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setDuration(fused.getValue());
		fusedRecord.setAttributeProvenance(Song.DURATION,
				fused.getOriginalIds());
	}

}
