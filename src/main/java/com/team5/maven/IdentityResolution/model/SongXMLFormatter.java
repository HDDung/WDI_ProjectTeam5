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
package com.team5.maven.IdentityResolution.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class SongXMLFormatter extends XMLFormatter<Song> {

	@Override
	public Element createRootElement(Document doc) {
		return doc.createElement("songs");
	}

	@Override
	public Element createElementFromRecord(Song record, Document doc) {
		Element song = doc.createElement("song");

		song.appendChild(createTextElement("id", record.getIdentifier(), doc));

		song.appendChild(createTextElementWithProvenance("name",
				record.getName(),
				record.getMergedAttributeProvenance(Song.NAME), doc));
		
		song.appendChild(createTextElementWithProvenance("artists",
				record.getArtist(),
				record.getMergedAttributeProvenance(Song.ARTIST), doc));
		
		song.appendChild(createTextElementWithProvenance("albums", record
				.getAlbum(), record
				.getMergedAttributeProvenance(Song.ALBUM), doc));
		
		song.appendChild(createTextElementWithProvenance("years",
				record.getYear(),
				record.getMergedAttributeProvenance(Song.YEAR), doc));
		
//		song.appendChild(createTextElementWithProvenance("duration",
//				record.getDuration(),
//				record.getMergedAttributeProvenance(Song.DURATION), doc));
		
		song.appendChild(createTextElementWithProvenance("genres", record
				.getGenre(), record
				.getMergedAttributeProvenance(Song.GENRE), doc));
		
		song.appendChild(createTextElementWithProvenance("recordLabel",
				record.getRecordLabel(),
				record.getMergedAttributeProvenance(Song.RECORDLABEL), doc));
		
		song.appendChild(createTextElementWithProvenance("producers",
				record.getProducer(),
				record.getMergedAttributeProvenance(Song.PRODUCER), doc));
		
		song.appendChild(createTextElementWithProvenance("writers", record
				.getWriter(), record
				.getMergedAttributeProvenance(Song.WRITER), doc));

		return song;
	}

	protected Element createTextElementWithProvenance(String name,
			String value, String provenance, Document doc) {
		Element elem = createTextElement(name, value, doc);
		elem.setAttribute("provenance", provenance);
		return elem;
	}

}
