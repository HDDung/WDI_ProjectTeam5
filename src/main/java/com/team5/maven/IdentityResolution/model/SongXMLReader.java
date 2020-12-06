package com.team5.maven.IdentityResolution.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class SongXMLReader extends XMLMatchableReader<Song, Attribute> implements
FusibleFactory<Song, Attribute>  {

	@Override
	protected void initialiseDataset(DataSet<Song, Attribute> dataset) {
		super.initialiseDataset(dataset);
		
		dataset.addAttribute(Song.NAME);
		dataset.addAttribute(Song.ARTIST);
		dataset.addAttribute(Song.ALBUM);
		dataset.addAttribute(Song.YEAR);
		dataset.addAttribute(Song.DURATION);
		dataset.addAttribute(Song.GENRE);
		dataset.addAttribute(Song.RECORDLABEL);
		dataset.addAttribute(Song.PRODUCER);
		dataset.addAttribute(Song.WRITER);
		
	}
	
	private static double parseStringToDouble(String value, double defaultValue) {
	    return value == null || value.isEmpty() ? defaultValue : Double.parseDouble(value);
	}
	
	@Override
	public Song createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "id");

		// create the object with id and provenance information
		Song song = new Song(id, provenanceInfo);

		// fill the attributes
		song.setName(getValueFromChildElement(node, "name"));
		
		song.setArtist(getValueFromChildElement(node, "artists"));
		
		song.setAlbum(getValueFromChildElement(node, "albums"));
		
		song.setGenre(getValueFromChildElement(node, "genres"));
		
		song.setDuration(parseStringToDouble(getValueFromChildElement(node, "duration"), -1));
		
		song.setRecordLabel(getValueFromChildElement(node, "recordLabel"));
		
		song.setProducer(getValueFromChildElement(node, "producers"));
		
		song.setWriter(getValueFromChildElement(node, "writers"));
		
		// convert the date string into a DateTime object
		try {
			String year = getValueFromChildElement(node, "years");
			if (year != null && !year.isEmpty()) {
				DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				        .appendPattern("yyyy-MM-dd")
				        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
				        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
				        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
				        .toFormatter(Locale.ENGLISH);
				LocalDateTime dt = LocalDateTime.parse(year, formatter);
				song.setYear(dt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return song;
	}
	
	@Override
	public Song createInstanceForFusion(RecordGroup<Song, Attribute> cluster) {
	
	List<String> ids = new LinkedList<>();
	
	for (Song s : cluster.getRecords()) {
		ids.add(s.getIdentifier());
	}
	
	Collections.sort(ids);
	
	String mergedId = StringUtils.join(ids, '+');
	
	return new Song(mergedId, "fused");
	}

}
