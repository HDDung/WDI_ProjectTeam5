/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
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
