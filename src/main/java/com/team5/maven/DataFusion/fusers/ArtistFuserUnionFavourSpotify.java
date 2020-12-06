package com.team5.maven.DataFusion.fusers;

import com.team5.maven.DataFusion.fusers.utility.UnionFavourSpotify;
import com.team5.maven.IdentityResolution.model.Song;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.WordUtils;

import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class ArtistFuserUnionFavourSpotify extends
		AttributeValueFuser<List<String>, Song, Attribute> {

	public ArtistFuserUnionFavourSpotify() {
		super(new UnionFavourSpotify<String, Song, Attribute>());
	}

	@Override
	public boolean hasValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Song.ARTIST);
	}

	@Override
	public List<String> getValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
		String artists = WordUtils.capitalizeFully(record.getArtist());
		return Arrays.asList(artists.split("\\s*,\\s*"));
	}

	@Override
	public void fuse(RecordGroup<Song, Attribute> group, Song fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<List<String>, Song, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setArtist(fused.getValue().toString().replace("[", "").replace("]", ""));
		fusedRecord.setAttributeProvenance(Song.ARTIST,
				fused.getOriginalIds());
	}

}
